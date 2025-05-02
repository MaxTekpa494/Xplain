const API_ENDPOINTS = {
    LIST_CONVERSATIONS: '/list',
    LLM: '/llm',
    COMPILER: '/compiler'
};

// Conversation Management
export const fetchConversations = async () => {
    try {
        const response = await fetch(`${API_ENDPOINTS.LIST_CONVERSATIONS}/conversations`);
        return await response.json();
    } catch (error) {
        console.error("Error loading conversations:", error);
        throw error;
    }
};

export const createNewConversation = async (title) => {
    try {
        const response = await fetch(`${API_ENDPOINTS.LIST_CONVERSATIONS}/conversation`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ title })
        });

        if (!response.ok) throw new Error('Failed to create conversation');
        return await response.json();
    } catch (error) {
        console.error('Error creating conversation:', error);
        throw error;
    }
};

export const loadConversation = async (id) => {
    try {
        const response = await fetch(`${API_ENDPOINTS.LIST_CONVERSATIONS}/conversation/${id}`);
        if (!response.ok) throw new Error('Failed to load conversation');
        return await response.json();
    } catch (error) {
        console.error('Error loading conversation:', error);
        throw error;
    }
};

export const updateConversationDate = async (conversationId) => {
    try {
        const response = await fetch(`${API_ENDPOINTS.LIST_CONVERSATIONS}/conversation/${conversationId}/update-date`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'}
        });
        if (!response.ok) throw new Error(await response.text());
    } catch (error) {
        console.error('Error updating conversation date:', error);
        throw error;
    }
};

export const updateConversationTitle = async (conversationId) => {
    try {
        await fetch(`${API_ENDPOINTS.LIST_CONVERSATIONS}/conversation/${conversationId}/update-title`, {
            method: "POST",
        });
    } catch (error) {
        console.error(`Error updating title (conversation ${conversationId}):`, error);
        throw error;
    }
};

export const editConversationTitle = async (conversationId, newTitle) => {
    try {
        await fetch(`${API_ENDPOINTS.LIST_CONVERSATIONS}/conversation/${conversationId}/edit-title`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ title: newTitle }),
        });
    } catch (error) {
        console.error(`Error editing conversation title ${conversationId}:`, error);
        throw error;
    }
};

export const deleteConversation = async (conversationId) => {
    try {
        await fetch(`${API_ENDPOINTS.LIST_CONVERSATIONS}/conversation/${conversationId}/delete`, {
            method: "DELETE",
        });
    } catch (error) {
        console.error(`Error deleting conversation ${conversationId}:`, error);
        throw error;
    }
};

// Message Management
export const saveMessage = async (conversationId, sender, content, errorsCompiler = '', responseLLM = '') => {
    try {
        const response = await fetch(`${API_ENDPOINTS.LIST_CONVERSATIONS}/conversation/message`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                conversationId,
                sender,
                content,
                errorsCompiler,
                responseLLM
            })
        });

        if (!response.ok) throw new Error('Failed to save message');
        return await response.json();
    } catch (error) {
        console.error('Error saving message:', error);
        throw error;
    }
};

// Model Management
export const fetchAvailableModels = async () => {
    try {
        const response = await fetch(`${API_ENDPOINTS.LLM}/models`);
        return await response.json();
    } catch (error) {
        console.error('Error fetching models:', error);
        throw error;
    }
};

export const setModel = async (modelType) => {
    try {
        await fetch(`${API_ENDPOINTS.LLM}/setModel/${modelType}`, {
            method: 'POST',
        });
    } catch (error) {
        console.error('Error changing model:', error);
        throw error;
    }
};

// Compiler Service
export const checkCode = async (content) => {
    try {
        const response = await fetch(`${API_ENDPOINTS.COMPILER}/check`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ content })
        });

        if (!response.ok) throw new Error('Compiler check failed');
        return await response.json();
    } catch (error) {
        console.error('Error in compilation process:', error);
        throw error;
    }
};