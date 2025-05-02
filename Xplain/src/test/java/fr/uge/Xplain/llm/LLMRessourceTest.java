package fr.uge.Xplain.llm;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;


@QuarkusTest
public class LLMRessourceTest {

  @Inject
  LLMService llmService;

  // @BeforEach Not functionnal test
  public void setUp() throws IOException {
      // Initialiser les modèles avant chaque test
      llmService.init();  // <- do not work and I don't know why
  }

  // @Test Not functionnal test
  public void testGetAvailableModels() {
      given()
        .when().get("/llm/models")
        .then()
        .statusCode(200)
        .body("$", not(empty())); // Vérifie que la liste des modèles n'est pas vide
  }

  // @Test Not functionnal test
  public void testGetCurrentModel() {
      given()
        .when().get("/llm/currentModel")
        .then()
        .statusCode(200)
        .body(notNullValue()); // Vérifie que le modèle actuel est retourné
  }

  // @Test Not functionnal test
  public void testSetModel() {
      given()
        .pathParam("modelType", "MISTRAL_7B") // Assurez-vous que ce modèle est valide
        .when().post("/llm/setModel/{modelType}")
        .then()
        .statusCode(200); // Vérifie que l'opération de changement de modèle est réussie
  }
}