package fr.uge.Xplain.compiler;

import org.junit.jupiter.api.Test;

import javax.tools.ToolProvider;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CompilerServiceTest {

  @Test
  void shouldCompileValidJavaClass() throws IOException {
    CompilerService compilerService = new CompilerService();

    String javaCode = """
            public class TestClass {
                public String hello() {
                    return "Hello, World!";
                }
            }
            """;

    var compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      throw new UnsupportedOperationException("Java Compiler is not available in this environment.");
    }

    String output = compilerService.compile(javaCode);

    assertEquals("", output);
  }

  @Test
  void shouldFailToCompileInvalidJavaCode() {
    CompilerService compilerService = new CompilerService();

    String javaCode = """
            public class TestClass {
                public String hello() {
                    return "Hello, World!"
                }
            }
            """;

    var compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      throw new UnsupportedOperationException("Java Compiler is not available in this environment.");
    }

    try {
      String output = compilerService.compile(javaCode);
      boolean containsError = output.contains("error");
      assertTrue(containsError);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void shouldThrowExceptionForNullJavaCode() {
    CompilerService compilerService = new CompilerService();

    assertThrows(NullPointerException.class, () -> {
      compilerService.compile(null);
    });
  }
}