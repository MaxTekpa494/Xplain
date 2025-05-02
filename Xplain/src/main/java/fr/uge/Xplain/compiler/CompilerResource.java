package fr.uge.Xplain.compiler;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

@Path("/compiler")
@RunOnVirtualThread
public class CompilerResource {

  /**
   * Service dependency responsible for handling the compilation of Java code.
   * This instance of {@code CompilerService} is injected into the {@code CompilerResource},
   * enabling functionality such as extracting class or record names, compiling Java code,
   * and managing compilation-related operations.
   */
  @Inject
  CompilerService compilerService;


  record Ask(String content) { }


  /**
   * Checks the provided Java content for compilation errors.
   *
   * This method receives a Java code snippet encapsulated in an {@code Ask} object,
   * compiles the provided code via the {@code CompilerService}, and returns the
   * compilation errors, if any, in a response.
   *
   * @param message the {@code Ask} object containing the Java code to be compiled.
   *                Its {@code content} field must contain the Java source code.
   * @return a {@code Response} object containing an {@code Ask} object with the compilation errors,
   *         or an empty string if no errors were found.
   * @throws IOException if an I/O error occurs during the compilation process.
   */
  @POST
  @Path("/check")
  public Response checkCompilerErrors(Ask message) throws IOException {
    var errorCompiler = compilerService.compile(message.content);
    return Response.ok(new Ask(errorCompiler)).build();
  }


}