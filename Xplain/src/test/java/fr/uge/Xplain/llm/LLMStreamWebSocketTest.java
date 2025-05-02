package fr.uge.Xplain.llm;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.websocket.*;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@ClientEndpoint
public class LLMStreamWebSocketTest {

  private Session session;
  private static final CountDownLatch messageLatch = new CountDownLatch(1);
  private static String receivedMessage;

  @BeforeEach
  public void connectToServer() {
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      session = container.connectToServer(this, URI.create("ws://localhost:8080/llm/stream"));
    } catch (Exception e) {
      fail("Failed to connect to server: " + e.getMessage());
    }
  }

  @AfterEach
  public void closeSession() {
    try {
      if (session != null) {
        session.close();
      }
    } catch (Exception e) {
      fail("Failed to close session: " + e.getMessage());
    }
  }

  @OnOpen
  public void onOpen(Session session) {
    assertTrue(session.isOpen());
  }

  @OnMessage
  public void onMessage(String message) {
    receivedMessage = message;
    messageLatch.countDown();
  }

  @OnClose
  public void onClose(Session session) {
    assertFalse(session.isOpen());
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    fail("Error occurred: " + throwable.getMessage());
  }

  // @Test Not functionnal test
  public void testOnOpen() {
    assertTrue(session.isOpen());
  }

  // @Test Not functionnal test
  public void testOnMessage() throws Exception {
    String testPayload = "{\"userMessage\":\"Test\", \"compilerErrors\":\"None\", \"model\":\"MISTRAL_7B\"}";
    session.getAsyncRemote().sendText(testPayload);

    assertTrue(messageLatch.await(5, TimeUnit.SECONDS));
    assertNotNull(receivedMessage);
    assertFalse(receivedMessage.isEmpty());
  }

  // @Test Not functionnal test
  public void testOnClose() throws Exception {
    session.close();
    assertFalse(session.isOpen());
  }

  // @Test Not functionnal test
  public void testOnError() {
    // Simulate an error by sending an invalid message
    session.getAsyncRemote().sendText(null);

    try {
      assertTrue(messageLatch.await(5, TimeUnit.SECONDS));
    } catch (InterruptedException e) {
      fail("Test interrupted: " + e.getMessage());
    }
    assertTrue(receivedMessage.contains("Erreur:"));
  }
}