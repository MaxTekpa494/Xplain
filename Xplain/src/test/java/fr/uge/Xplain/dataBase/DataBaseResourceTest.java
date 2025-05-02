package fr.uge.Xplain.dataBase;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class DataBaseResourceTest {

  @Inject
  DataBaseService dataBaseService;

  @Inject
  DatabaseInitializer databaseInitializer;

  @BeforeEach
  public void setUp() {
    databaseInitializer.init();
  }

  @Test
  void getAllConversationsTest() {
    given()
        .when().get("/list/conversations")
        .then()
        .statusCode(200)
        .body("$", not(empty())); // Vérifie que la liste n'est pas vide
  }

  @Test
  void createConversationTest() {
    given()
        .contentType("application/json")
        .body("{\"title\": \"Nouvelle Discussion\"}")
        .when().post("/list/conversation")
        .then()
        .statusCode(200)
        .body("title", equalTo("Nouvelle Discussion")); // Vérifie que le titre est correct
  }




  @Test
  void updateConversationTitleTest() {
    long conversationId = 1; // s'assure que cette conversation existe
    given()
        .when().post("/list/conversation/" + conversationId + "/update-title")
        .then()
        .statusCode(200)
        .body(equalTo("The conversation title has been updated successfully.")); // Vérifie le message de succès
  }

  @Test
  void updateConversationDateTest() {
    long conversationId = 1; // s'assure que cette conversation existe
    given()
        .when().post("/list/conversation/" + conversationId + "/update-date")
        .then()
        .statusCode(200)
        .body(equalTo("The conversation date has been updated successfully.")); // Vérifie le message de succès
  }

  @Test
  void deleteConversationTest() {
    long conversationId = 1; // s'assure que cette conversation existe
    given()
        .when().delete("/list/conversation/" + conversationId + "/delete")
        .then()
        .statusCode(200)
        .body(equalTo("The conversation has been deleted successfully.")); // Vérifie le message de succès
  }

  @Test
  void editConversationTitleTest() {
    long conversationId = 1; // s'assure que cette conversation existe
    given()
        .contentType("application/json")
        .body("{\"title\": \"Updated Title\"}")
        .when().post("/list/conversation/" + conversationId + "/edit-title")
        .then()
        .statusCode(200)
        .body(equalTo("The conversation title has been updated successfully.")); // Vérifie le message de succès
  }
}
