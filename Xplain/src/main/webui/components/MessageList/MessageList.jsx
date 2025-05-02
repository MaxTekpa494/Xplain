import {createSignal, createEffect, For} from "solid-js";
import { useNavigate } from '@solidjs/router';
import {
    deleteConversation,
    editConversationTitle,
    fetchConversations,
    updateConversationTitle
} from "../../services/serviceIA";

const MessageList = () => {
    const [conversations, setConversations] = createSignal([]); // Déclare un tableau de conversations
    const [editingId, setEditingId] = createSignal(null);
    const [editTitle, setEditTitle] = createSignal('');
    const [searchQuery, setSearchQuery] = createSignal('');
    const navigate = useNavigate();

    const loadHistory = async () => {
        try {
            const history = await fetchConversations(); // Utilisation de la fonction importée
            for (const conversation of history) {
                if (conversation.title === 'Nouvelle conversation') {
                    await updateConversationTitle(conversation.id); // Appelle la fonction importée
                }
            }
            setConversations(history); // Met à jour les conversations
        } catch (error) {
            console.error('Erreur lors du chargement des conversations :', error);
        }
    };

    const filteredConversations = () => {
        return conversations().filter(conv =>
            conv.title.toLowerCase().includes(searchQuery().toLowerCase()) ||
            conv.description?.toLowerCase().includes(searchQuery().toLowerCase())
        );
    };


    const handleEditTitle = async (conversationId, newTitle) => {
        try {
            await editConversationTitle(conversationId, newTitle);
            setConversations(prev =>
                prev.map(conv =>
                    conv.id === conversationId ? { ...conv, title: newTitle } : conv
                )
            );
            setEditingId(null);
        } catch (error) {
            console.error(`Erreur lors de la mise à jour du titre :`, error);
        }
    };


    const handleDeleteConversation = async (conversationId, event) => {
        event.stopPropagation();
        try {
            await deleteConversation(conversationId);
            setConversations(prev => prev.filter(conv => conv.id !== conversationId));
        } catch (error) {
            console.error(`Erreur lors de la suppression :`, error);
        }
    };

    createEffect(() => {
        loadHistory();
    });

    const handleNewChat = () => {
        navigate('/chat');
    };

    const handleSelectChat = (conversation) => {
        if (editingId() !== conversation.id) {
            navigate(`/chat/${conversation.id}`);
        }
    };

    const startEditing = (conversation, event) => {
        event.stopPropagation();
        setEditingId(conversation.id);
        setEditTitle(conversation.title);
    };



    const handleKeyboardEvent = (conversationId, event) => {
        if (event.key === 'Enter' || event.code === 'Enter' || event.keyCode === 13) {
            event.preventDefault();
            handleEditTitle(conversationId, editTitle());
        } else if (event.key === 'Escape') {
            setEditingId(null);
        }
    };


    return (
        <div class="section" style={{ background: '#f5f5f5' }}>
            <div class="container">
                {/* En-tête et recherche */}
                <div class="columns is-variable is-8">
                    <div class="column is-two-thirds">
                        <h1 class="title is-2 mb-6">Historique des Discussions</h1>
                        <div class="field mb-6">
                            <div class="control has-icons-left">
                                <input
                                    class="input is-medium"
                                    type="text"
                                    placeholder="Rechercher une discussion..."
                                    value={searchQuery()}
                                    onInput={(e) => setSearchQuery(e.target.value)}
                                    style={{
                                        'border-radius': '6px',
                                        'box-shadow': 'none',
                                        'border': '2px solid #ededed'
                                    }}
                                />
                                <span class="icon is-left">
                                    <i class="fas fa-search"></i>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="column">
                        <button
                            class="button is-primary is-medium is-pulled-right"
                            onClick={handleNewChat}
                            style={{
                                'border-radius': '6px',
                                'background-color': '#4a4a4a',
                                'border': 'none',
                                'box-shadow': '0 2px 4px rgba(0,0,0,0.1)'
                            }}
                        >
                            <span class="icon">
                                <i class="fas fa-plus"></i>
                            </span>
                            <span>Nouvelle Discussion</span>
                        </button>
                    </div>
                </div>

                {/* Grille des conversations */}
                <div class="columns is-multiline is-variable is-4">
                    <For each={filteredConversations()}>
                        {(conversation) => (
                            <div class="column is-one-third">
                                <div
                                    class="box p-5"
                                    onClick={() => handleSelectChat(conversation)}
                                    style={{
                                        'border-radius': '8px',
                                        'box-shadow': 'none',
                                        'border': '1px solid #ededed',
                                        'background': '#ffffff',
                                        'transition': 'all 0.2s ease',
                                        'cursor': 'pointer',
                                        'min-height': '180px',
                                        'position': 'relative'
                                    }}
                                    onMouseOver={(e) => {
                                        e.currentTarget.style.transform = 'translateY(-2px)';
                                        e.currentTarget.style.boxShadow = '0 4px 8px rgba(0,0,0,0.1)';
                                    }}
                                    onMouseOut={(e) => {
                                        e.currentTarget.style.transform = 'translateY(0)';
                                        e.currentTarget.style.boxShadow = 'none';
                                    }}
                                >
                                    {editingId() === conversation.id ? (
                                        <input
                                            className="input is-medium"
                                            type="text"
                                            value={editTitle()}
                                            onChange={(e) => setEditTitle(e.target.value)}
                                            onKeyDown={(e) => handleKeyboardEvent(conversation.id, e)}
                                            onKeyPress={(e) => handleKeyboardEvent(conversation.id, e)}
                                            onBlur={() => handleEditTitle(conversation.id, editTitle())}
                                            onClick={(e) => e.stopPropagation()}
                                            autofocus
                                            style={{
                                                'border-radius': '4px',
                                                'box-shadow': 'none',
                                                'border': '2px solid #ededed'
                                            }}
                                        />

                                    ) : (
                                        <>
                                            <h3 class="title is-4 mb-4" style={{'color': '#363636'}}>
                                                {conversation.title}
                                            </h3>
                                            <p class="subtitle is-6 has-text-grey">
                                                {conversation.description || 'Aucune description'}
                                            </p>
                                        </>
                                    )}

                                    <div
                                        class="buttons are-small"
                                        style={{
                                            position: 'absolute',
                                            bottom: '1.25rem',
                                            right: '1.25rem',
                                            opacity: '0.8'
                                        }}
                                    >
                                        <button
                                            class="button is-light"
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                startEditing(conversation, e);
                                            }}
                                            title="Renommer"
                                        >
                                            <span class="icon">
                                                <i class="fas fa-edit"></i>
                                            </span>
                                        </button>
                                        <button
                                            class="button is-light"
                                            onClick={(e) => handleDeleteConversation(conversation.id, e)}
                                            title="Supprimer"
                                        >
                                            <span class="icon has-text-danger">
                                                <i class="fas fa-trash-alt"></i>
                                            </span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        )}
                    </For>
                </div>
            </div>
        </div>
    );
};

export default MessageList;