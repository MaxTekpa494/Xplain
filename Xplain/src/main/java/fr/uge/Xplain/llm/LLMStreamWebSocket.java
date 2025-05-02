package fr.uge.Xplain.llm;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.logging.Logger;

@ServerEndpoint("/llm/stream")
@RunOnVirtualThread
public class LLMStreamWebSocket {

  /**
   * Logger instance used for logging messages and errors within the LLMStreamWebSocket class.
   * This logger is configured to use the class name as a source for log messages, enabling
   * effective tracking and debugging of WebSocket events and operations such as connections,
   * errors, and message handling.
   */
  private static final Logger LOG = Logger.getLogger(LLMStreamWebSocket.class.getName());

  /**
   * Injected service responsible for handling large language model (LLM) operations.
   *
   * This service provides functionalities such as managing LLM models, generating
   * responses based on provided prompts, and handling streaming outputs for client sessions.
   * It is used within the WebSocket endpoint to process Java code inputs and generate
   * LLM-driven outputs.
   */
  @Inject
  LLMService llmService;



  /**
   * Invoked when a new WebSocket connection is opened. Establishes a session
   * between the client and the server.
   *
   * @param session the WebSocket session object representing the connection
   */
  @OnOpen
  public void onOpen(Session session) {
    session.isOpen();
  }


  /**
   * Handles incoming WebSocket messages from the client. This method processes the message
   * by forwarding it to the large language model (LLM) service, which generates and streams
   * responses back to the client.
   *
   * @param javaClassContent the payload received from the client containing user input and
   *                         compiler error messages. Must not be null.
   * @param session          the WebSocket session used for the exchange of messages between
   *                         the client and the server.
   * @throws IOException if an I/O error occurs while sending a message through the WebSocket session.
   */
  @OnMessage
  public void onMessage(String javaClassContent, Session session) throws IOException {
    if (javaClassContent == null) {
      session.getBasicRemote().sendText("Erreur: Contenu vide");
      return;
    }

    ObjectMapper objectMapper = new ObjectMapper();
    UserCompilerPayload compilerPayload = objectMapper.readValue(javaClassContent, UserCompilerPayload.class);

    llmService.generateResponseStream(compilerPayload, session);

  }

  /**
   * Called when the WebSocket connection is closed.
   *
   * @param session the WebSocket session associated with the connection that was closed
   */
  @OnClose
  public void onClose(Session session) throws IOException {
    session.close();
  }

  /**
   * Handles errors that occur during the WebSocket interaction.
   * Sends an error message to the client containing the error description.
   *
   * @param session   the WebSocket session associated with the client where the error occurred
   * @param throwable the exception or error that occurred
   * @throws IOException if an I/O error occurs while sending the error message to the client
   */
  @OnError
  public void onError(Session session, Throwable throwable) throws IOException {
    session.getAsyncRemote().sendText("Erreur: " + throwable.getMessage());
  }
}