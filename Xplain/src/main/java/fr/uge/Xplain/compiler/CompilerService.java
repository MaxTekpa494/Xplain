package fr.uge.Xplain.compiler;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@ApplicationScoped
@RunOnVirtualThread
public class CompilerService {


  /**
   * Extracts the name of a class, interface, enum, record, or generic class
   * from the provided Java source code.
   *
   * Uses regular expressions to find the first matching declaration name.
   *
   * @param javaCode the Java source code as a string
   * @return the name of the declarative type (class, interface, etc.) if found, or null otherwise
   */
  private String extractClassName(String javaCode) {
    // Regex to match class, interface, enum, record, or parameterized class declarations
    String regex = "\\b(class|interface|enum|record)\\s+(\\w+)|\\bclass\\s+<[^>]+>\\s+(\\w+)";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(javaCode);

    // Check for a match
    if (matcher.find()) {
      // Return the name of the matched class, enum, etc.
      if (matcher.group(2) != null) {
        return matcher.group(2); // For normal declarations
      } else if (matcher.group(3) != null) {
        return matcher.group(3);
      }
    }
    return null;
  }


  /**
   * Compiles the given Java source code and returns the compiler output.
   *
   * The method uses the system's Java compiler to compile the input source code. It extracts
   * the class or record name from the source code to create a source file representation,
   * sets up a file manager, and writes the compiler output to a {@code StringWriter}.
   * The output of the compilation, including error messages if present, is returned as a string.
   *
   * @param codeJava the Java source code to be compiled. Must not be null.
   * @return the compiler output, including any compilation errors or warnings.
   * @throws IOException if an I/O error occurs during the file management or compilation process.
   */
  public String compile(String codeJava) throws IOException {
    Objects.requireNonNull(codeJava);
    var className       = extractClassName(codeJava);
    var compiler        = ToolProvider.getSystemJavaCompiler();
    var fileManager     = compiler.getStandardFileManager(null, null, null);
    var targetDirectory = "...";
    var compilerOutput  = new StringWriter();
    var printWriter     = new PrintWriter(compilerOutput);
    var task = compiler.getTask(printWriter,
            fileManager,
            null,
            List.of("-d", targetDirectory),
            null,
            List.of(SimpleJavaFileObject.forSource(URI.create(className + ".java"), codeJava))).call();
    printWriter.close();
    fileManager.close();
    return compilerOutput.toString();
  }
}