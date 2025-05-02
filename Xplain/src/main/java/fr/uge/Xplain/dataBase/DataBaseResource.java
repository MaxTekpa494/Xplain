package fr.uge.Xplain.dataBase;

import fr.uge.Xplain.dataBase.conversation.Conversation;
import fr.uge.Xplain.dataBase.message.Message;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Logger;

@Path("/list")
@RunOnVirtualThread
public class DataBaseResource {

    private static final Logger LOG = Logger.getLogger(DataBaseResource.class.getName());

    /**
     * This field represents an instance of the DataBaseService, which is responsible
     * for handling database interactions related to conversations and messages. It
     * is injected to provide access to various methods for creating, updating, and
     * retrieving conversation and message data.
     */
    @Inject
    DataBaseService dataBaseService;

    /**
     * Retrieves a list of all conversations from the database.
     *
     * @return a Response object containing a list of Conversation records.
     *         The list may be empty if there are no conversations or an error occurs
     *         during data fetching.
     */
    @GET
    @Path("/conversations")
    @Produces("application/json")
    public Response getAllConversations() {
        List<Conversation> conversations = dataBaseService.getAllConversations();
        return Response.ok(conversations).build();
    }

    /**
     * Creates a new conversation with the given title and stores it in the database.
     *
     * @param request The {@code ConversationRequest} object containing the title for the new conversation.
     * @return A {@code Response} containing the created {@code Conversation} object.
     */
    @POST
    @Path("/conversation")
    @Produces("application/json")
    public Response createConversation(ConversationRequest request) {
        Conversation conversation = dataBaseService.createConversation(request.title);
        return Response.ok(conversation).build();
    }

    /**
     * Retrieves all messages associated with a specific conversation.
     *
     * @param conversationId the unique identifier of the conversation whose messages are to be retrieved
     * @return a Response object containing a list of Message records associated with the given conversationId
     */
    @GET
    @Path("/conversation/{conversationId}")
    @Produces("application/json")
    public Response getConversationMessages(@PathParam("conversationId") long conversationId) {
        List<Message> messages = dataBaseService.getConversationMessages(conversationId);
        return Response.ok(messages).build();
    }


    /**
     * Adds a message to a specific conversation and responds with the created message.
     *
     * @param request the details of the message to be added, including conversation ID, sender,
     *                content, errorsCompiler, and responseLLM
     * @return a Response object containing the newly created Message
     */
    @POST
    @Path("/conversation/message")
//    @Produces("application/json")
//    @Consumes("application/json")
    public Response addMessageToConversation(MessageRequest request) {
        LOG.info("Affichage de l'ID du message : " + request.conversationId);
        Message message = dataBaseService.addMessageToConversation(
                request.conversationId,
                request.sender,
                request.content,
                request.errorsCompiler,
                request.responseLLM);
        return Response.ok(message).build();
    }


    /**
     * Updates the title of a conversation using the content of its first message.
     * The new title is derived from the first 25 characters (or fewer if the message is shorter)
     * of the first message associated with the conversation.
     *
     * @param conversationId the unique identifier of the conversation for which the title is to be updated
     * @return a Response indicating that the conversation title has been updated successfully
     */
    @POST
    @Path("/conversation/{conversationId}/update-title")
    //@Produces("application/json")
    public Response updateConversationTitle(@PathParam("conversationId") long conversationId) {
        dataBaseService.updateConversationTitleWithFirstMessage(conversationId);
        return Response.ok("The conversation title has been updated successfully.").build();
    }


    /**
     * Updates the last modified date of a conversation identified by its ID.
     *
     * @param conversationId the ID of the conversation whose date needs to be updated
     * @return a Response object indicating the success of the operation
     */
    @POST
    @Path("/conversation/{conversationId}/update-date")
    @Produces("application/json")
    public Response updateConversationDate(@PathParam("conversationId") long conversationId) {
        dataBaseService.updateConversationDate(conversationId);
        return Response.ok("The conversation date has been updated successfully.").build();
    }

    /**
     * Deletes a conversation specified by its unique identifier.
     *
     * @param conversationId the unique identifier of the conversation to be deleted
     * @return a Response indicating the success of the deletion operation
     */
    @DELETE
    @Path("/conversation/{conversationId}/delete")
    //@Produces("application/json")
    public Response deleteConversation(@PathParam("conversationId") long conversationId) {
        dataBaseService.deleteConversation(conversationId);
        return Response.ok("The conversation has been deleted successfully.").build();
    }

    /**
     * Edits the title of an existing conversation in the database.
     *
     * @param conversationId the unique identifier of the conversation whose title is to be updated
     * @param conversationRequest a {@code ConversationRequest} object containing the new title of the conversation
     * @return a {@code Response} indicating that the conversation title has been successfully updated
     */
    @POST
    @Path("/conversation/{conversationId}/edit-title")
    @Produces("application/json")
    public Response editConversationTitle(@PathParam("conversationId") long conversationId, ConversationRequest conversationRequest) {
        dataBaseService.updateConversationTitle(conversationId, conversationRequest.title);
        return Response.ok("The conversation title has been updated successfully.").build();
    }


    public record ConversationRequest(String title) {
    }

    public record MessageRequest (long conversationId, String sender, String content, String errorsCompiler, String responseLLM){
    }
}