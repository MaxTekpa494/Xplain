
import { MessageComponent } from './MessageComponent';
import { ChatToolbar } from './ChatToolbar';
import { ChatInput } from './ChatInput';
import {createSignal, createEffect, For} from 'solid-js';
import { useNavigate, useParams } from '@solidjs/router';
import {
    checkCode,
    createNewConversation, deleteConversation,
    fetchAvailableModels,
    loadConversation,
    saveMessage, setModel,
    updateConversationDate
} from "../../services/serviceIA";

const ChatInterface = () => {
    const navigate = useNavigate();
    const params = useParams();
    const [messages, setMessages] = createSignal([]);
    const [inputMessage, setInputMessage] = createSignal('');
    const [availableModels, setAvailableModels] = createSignal([]);
    const [currentModel, setCurrentModel] = createSignal(null);
    const [titleOfConversation, setTitleOfConversation] = createSignal('Nouvelle conversation');
    const [isStreaming, setIsStreaming] = createSignal(false);
    const [webSocket, setWebSocket] = createSignal(null);
    const [currentConversation, setCurrentConversation] = createSignal(null);
    const [isInitialized, setIsInitialized] = createSignal(false);
    const [tilleOfConservation, settilleOfConservation] = createSignal(null);
    const [conversationId, setConversationId] = createSignal(null);
    const [isWebSocketOpen, setIsWebSocketOpen] = createSignal(false);

    // Initialisation WebSocket
    const openWebSocket = () => {
        if (isWebSocketOpen()) return;
        const socket = new WebSocket('ws://localhost:8080/llm/stream');
        let currentStreamingMessage = '';

        socket.onopen = () => {
            console.log('WebSocket connection opened');
            setWebSocket(socket);
            setIsWebSocketOpen(true);
        };

        socket.onmessage = async (event) => {
            if (event.data === '[END]') {
                setIsStreaming(false);
                if (currentConversation()) {
                    await saveMessage(conversationId(), 'Marx', currentStreamingMessage);
                }
                currentStreamingMessage = '';
                await updateConversationDate(conversationId());
                if (webSocket()) {
                    webSocket().close();
                    setIsWebSocketOpen(false);
                }
                return;
            }

            currentStreamingMessage += event.data;
            setMessages(prev => {
                const updated = [...prev];
                const lastIndex = updated.length - 1;
                if (lastIndex >= 0 && updated[lastIndex].sender === 'Marx') {
                    updated[lastIndex] = {
                        ...updated[lastIndex],
                        content: currentStreamingMessage
                    };
                }
                return updated;
            });
        };

        socket.onerror = (error) => {
            console.error('WebSocket Error:', error);
            setIsStreaming(false);
            if (webSocket()) {
                webSocket().close();
                setIsWebSocketOpen(false);
            }
        };

        socket.onclose = () => {
            console.log('WebSocket Connection closed');
            setIsStreaming(false);
            setIsWebSocketOpen(false);
        };

        setWebSocket(null);
        return () => socket.close();
    };

    // Initialize conversation
    createEffect(async () => {
        if (isInitialized()) return;

        try {
            setIsInitialized(true);
            const models = await fetchAvailableModels();
            setAvailableModels(models);

            const current = models.length > 0 ? models[1] : null;
            setCurrentModel(current);

            if (params.id) {
                console.log('reload params.id : ', params.id);
                setConversationId(params.id);
                const conversationData = await loadConversation(params.id);
                if (conversationData.messages) {
                    setMessages(conversationData.messages);
                } else if (Array.isArray(conversationData)) {
                    setMessages(conversationData);
                } else {
                    console.error("Unexpected data format:", conversationData);
                    setMessages([]);
                }
                setTitleOfConversation(conversationData.title || 'Loaded Conversation');
                setCurrentConversation(conversationData);
            } else {
                const conversation = await createNewConversation(titleOfConversation());
                setConversationId(conversation.id);
                setCurrentConversation(conversation);
                navigate(`/chat/${conversation.id}`);
            }
        } catch (error) {
            console.error('Error during initialization:', error);
            navigate('/');
        }
    });

    const handleModelChange = async (modelType) => {
        try {
            await setModel(modelType);
            setCurrentModel(modelType);
        } catch (error) {
            console.error('Error changing model:', error);
        }
    };

    const exitConversation = async () => {
        try {
            if(webSocket()){
                webSocket().close();
                setIsWebSocketOpen(false);
            }
           // await webSocket().close();
            navigate('/');
        } catch (error) {
            console.error('Error deleting conversation:', error);
        }
    }

    const sendClassToCompiler = async () => {
        if (inputMessage().trim() === '' || isStreaming() || !currentConversation()) return;

        if (!isWebSocketOpen()) {
            openWebSocket();
        }
        const userContent = inputMessage();
        setInputMessage('');

        try {
            const userMessage = await saveMessage(conversationId(), 'User', userContent);
            if (userMessage) {
                setMessages(prev => [...prev, userMessage]);
            }

            const compilerErrors = await checkCode(userContent);

            if (compilerErrors.content === '') {
                const compilerMessage = await saveMessage(conversationId(), 'Compiler', "Your code is correct.");
                if (compilerMessage) {
                    setMessages(prev => [...prev, compilerMessage]);
                }
                if (webSocket()) {
                    webSocket().close();
                    setIsWebSocketOpen(false);
                }
            } else {
                const compilerMessage = await saveMessage(conversationId(), 'Compiler', compilerErrors.content);
                if(compilerMessage) {
                    setMessages(prev => [...prev, compilerMessage]);
                }

                setMessages(prev => [...prev, {
                    sender: 'Marx',
                    content: '',
                    createdAt: new Date().toISOString()
                }]);

                const infoSocket = {
                    userMessage: userContent,
                    compilerErrors: compilerErrors.content,
                    model: currentModel()
                };

                if (webSocket()) {
                    console.log("Socket info:", JSON.stringify(infoSocket));
                    webSocket().send(JSON.stringify(infoSocket));
                    setIsStreaming(true);
                }
            }
        } catch (error) {
            console.error('Error in compilation process:', error);
            setMessages(prev => [...prev, {
                sender: 'System',
                content: 'An error occurred while processing your message.',
                createdAt: new Date().toISOString()
            }]);
        }
    };

    const handleEditMessage = async (messageId, newContent, isLLMEdit = false) => {
        try {
            if (!isWebSocketOpen()) {
                openWebSocket();
            }

            setMessages(prev =>
                prev.map(msg =>
                    msg.id === messageId ? {...msg, content: newContent} : msg
                )
            );

            const userMessage = await saveMessage(conversationId(), 'User', newContent);
            if (userMessage) {
                setMessages(prev => [...prev, userMessage]);
            }

            const compilerErrors = await checkCode(newContent);

            if (compilerErrors.content === '') {
                const compilerMessage = await saveMessage(conversationId(), 'Compiler', "Your code is correct.");
                if (compilerMessage) {
                    setMessages(prev => [...prev, compilerMessage]);
                }
                if (webSocket()) {
                    webSocket().close();
                    setIsWebSocketOpen(false);
                }
            } else {
                const compilerMessage = await saveMessage(conversationId(), 'Compiler', compilerErrors.content);
                if (compilerMessage) {
                    setMessages(prev => [...prev, compilerMessage]);
                }

                setMessages(prev => [...prev, {
                    sender: 'Marx',
                    content: '',
                    createdAt: new Date().toISOString()
                }]);

                const infoSocket = {
                    userMessage: newContent,
                    compilerErrors: compilerErrors.content,
                    model: currentModel()
                };

                if (webSocket()) {
                    webSocket().send(JSON.stringify(infoSocket));
                    setIsStreaming(true);
                }
            }
        } catch (error) {
            console.error('Error updating message:', error);
            setMessages(prev => [...prev, {
                sender: 'System',
                content: 'An error occurred while processing your message.',
                createdAt: new Date().toISOString()
            }]);
        }
    };

    return (
        <div
            class="container is-fluid"
            style={{
                'padding': '0',
                'height': '100vh',
                'display': 'flex',
                'flex-direction': 'column',
                'background': '#ffffff'  // Fond blanc uniforme
            }}
        >
            <div
                style={{
                    'flex-grow': 1,
                    'display': 'flex',
                    'flex-direction': 'column',
                    'overflow': 'hidden',
                }}
            >
                <ChatToolbar
                    title={params.id ? tilleOfConservation() : 'Nouvelle Discussion'}
                    currentModel={currentModel}
                    availableModels={availableModels}
                    onModelChange={handleModelChange}
                    //onBack={() => navigate('/')}
                    onBack={() => exitConversation()}
                />

                <div
                    class="message-list"
                    style={{
                        'flex-grow': 1,
                        'overflow-y': 'auto',
                        'padding': '1rem',
                        'background': '#ffffff'  // Fond blanc pour la liste des messages
                    }}
                >
                    <For each={messages()}>
                        {(message) => (
                            <MessageComponent
                                message={message}
                                onEdit={(newContent, isLLMEdit) => handleEditMessage(message.id, newContent, isLLMEdit)}
                            />
                        )}
                    </For>
                </div>

                <div style={{'padding': '1rem', 'background': '#ffffff'}}>
                    <ChatInput
                        inputMessage={inputMessage}
                        onInput={setInputMessage}
                        isStreaming={isStreaming}
                        onSend={sendClassToCompiler}
                    />
                </div>
            </div>
        </div>
    );

}

export default ChatInterface;