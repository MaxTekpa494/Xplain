package fr.uge.Xplain.compiler;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import fr.uge.Xplain.compiler.CompilerResource.Ask;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class CompilerResourceTest {

  @Inject
  CompilerResource compilerResource;

  @Test
  void testCheckCompilerErrors_WhenValidRequest_ReturnsOkResponse() throws IOException {
    // Arrange
    var inputMessage = new Ask("""
            public class Hello {
                public static void main(String[] args) {
                    System.out.println("Hello");
                }
            }""");

    // Act
    Response response = compilerResource.checkCompilerErrors(inputMessage);

    // Assert
    assertEquals(200, response.getStatus(), "Expected HTTP status 200");
    Ask responseBody = (Ask) response.getEntity();
    assertEquals("", responseBody.content(), "Expected no compiler errors");
  }


  @Test
  void testCheckCompilerErrors_WhenEmptyContent_ReturnsEmptyMessage() throws IOException {
    // Arrange
    var inputMessage = new Ask("");

    // Act
    Response response = compilerResource.checkCompilerErrors(inputMessage);

    // Assert
    assertEquals(200, response.getStatus(), "Expected HTTP status 200");
    Ask responseBody = (Ask) response.getEntity();
    assertEquals("", responseBody.content(), "Expected empty result for empty input");
  }
}
