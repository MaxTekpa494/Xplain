package fr.uge.Xplain.dataBase;

import fr.uge.Xplain.dataBase.conversation.Conversation;
import fr.uge.Xplain.dataBase.message.Message;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataBaseServiceTest {

  @Inject
  DataBaseService dataBaseService;

  @Test
  @Order(1)
  @TestTransaction
  void testCreateConversation() {
    String title = "Nouvelle conversation";
    Conversation conversation = dataBaseService.createConversation(title);

    assertNotNull(conversation, "La conversation ne doit pas être nulle");
    assertEquals(title, conversation.title(), "Le titre n'est pas correctement enregistré");
    assertNotNull(conversation.createdAt(), "La date de création doit être auto-générée");
  }

  @Test
  @Order(2)
  @TestTransaction
  void testAddMessageToConversation() {
    Conversation conversation = dataBaseService.createConversation("Conversation avec messages");
    String sender = "Utilisateur";
    String content = "Ceci est un message de test";
    String responseLLM = "Réponse générée";

    Message message = dataBaseService.addMessageToConversation(
            conversation.id(),
            sender,
            content,
            null,
            responseLLM
    );

    assertNotNull(message, "Le message ne doit pas être nul");
    assertEquals(conversation.id(), message.conversationId(), "L'ID de la conversation doit correspondre");
    assertEquals(sender, message.sender(), "L'expéditeur doit correspondre");
    assertEquals(content, message.content(), "Le contenu du message doit correspondre");
    assertEquals(responseLLM, message.responseLLM(), "La réponse LLM doit correspondre");
  }

  @Test
  @Order(3)
  @TestTransaction
  void testGetConversationMessages() {
    Conversation conversation = dataBaseService.createConversation("Conversation test");
    dataBaseService.addMessageToConversation(conversation.id(), "User1", "Message 1", null, "Response 1");
    dataBaseService.addMessageToConversation(conversation.id(), "User2", "Message 2", null, "Response 2");

    List<Message> messages = dataBaseService.getConversationMessages(conversation.id());

    assertNotNull(messages, "La liste des messages ne doit pas être nulle");
    assertEquals(2, messages.size(), "Il doit y avoir 2 messages dans la conversation");
    assertEquals("Message 1", messages.get(0).content(), "Le contenu du premier message doit être correct");
    assertEquals("Message 2", messages.get(1).content(), "Le contenu du deuxième message doit être correct");
  }

  @Test
  @Order(4)
  @TestTransaction
  void testGetAllConversations() {
    dataBaseService.createConversation("Conversation 1");
    dataBaseService.createConversation("Conversation 2");

    List<Conversation> conversations = dataBaseService.getAllConversations();

    assertNotNull(conversations, "La liste des conversations ne doit pas être nulle");
    assertTrue(conversations.size() >= 2, "Il doit y avoir au moins 2 conversations");
  }


  @Test
  @Order(5)
  @TestTransaction
  void testUpdateConversationTitle() {
    Conversation conversation = dataBaseService.createConversation("Titre initial");

    dataBaseService.updateConversationTitle(conversation.id(), "Titre modifié");

    Conversation updatedConversation = dataBaseService.getConversationById(conversation.id());
    assertNotNull(updatedConversation, "La conversation mise à jour ne doit pas être nulle");
    assertEquals("Titre modifié", updatedConversation.title(), "Le titre n'a pas été correctement mis à jour");
  }



  @Test
  @Order(6)
  @TestTransaction
  void testDeleteConversation() {
    Conversation conversation = dataBaseService.createConversation("Conversation à supprimer");

    dataBaseService.deleteConversation(conversation.id());

    Conversation deletedConversation = dataBaseService.getConversationById(conversation.id());

  }

  @Test
  @Order(7)
  @TestTransaction
  void testUpdateConversationTitleWithFirstMessage() {
    Conversation conversation = dataBaseService.createConversation("Conversation pour test");
    String sender = "Utilisateur";
    dataBaseService.addMessageToConversation(conversation.id(), sender, "Premier message", null, "Réponse 1");

    dataBaseService.updateConversationTitleWithFirstMessage(conversation.id());

    Conversation updatedConversation = dataBaseService.getConversationById(conversation.id());
    assertNotNull(updatedConversation, "La conversation mise à jour ne doit pas être nulle");
    assertEquals("Premier message", updatedConversation.title(),
            "Le titre de la conversation doit correspondre au contenu du premier message");
  }

  @Test
  @Order(8)
  @TestTransaction
  void testUpdateAndDeleteConversations() {
    Conversation conversation1 = dataBaseService.createConversation("Conversation 1");
    Conversation conversation2 = dataBaseService.createConversation("Conversation 2");

    dataBaseService.updateConversationTitle(conversation1.id(), "Titre mis à jour 1");

    dataBaseService.deleteConversation(conversation2.id());

    Conversation updatedConversation1 = dataBaseService.getConversationById(conversation1.id());
    assertNotNull(updatedConversation1, "La première conversation mise à jour ne doit pas être nulle");
    assertEquals("Titre mis à jour 1", updatedConversation1.title(),
            "Le titre de la première conversation doit être mis à jour");

  }


}
