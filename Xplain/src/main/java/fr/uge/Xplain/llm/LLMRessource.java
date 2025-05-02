package fr.uge.Xplain.llm;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;



@Path("/llm")
@RunOnVirtualThread
public class LLMRessource {

  /**
   * Service component responsible for managing interactions with the LLM (Large Language Model).
   * Provides functionalities such as retrieving available models, getting the current active model,
   * and setting a specific model to be used.
   */
  @Inject
  LLMService llmService;



  /**
   * Retrieves all available models from the LLM service.
   *
   * @return a Response containing a list of available models in JSON format.
   */
  @GET
  @Path("/models")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAvailableModels() {
    return Response.ok(llmService.getAvailableModels()).build();
  }

  /**
   * Retrieves the currently active language model.
   *
   * This endpoint returns the model currently in use by the service in JSON format.
   *
   * @return a {@code Response} containing the current language model information in JSON format.
   */
  @GET
  @Path("/currentModel")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCurrentModel() {
    return Response.ok(llmService.getCurrentModel()).build();
  }

  /**
   * Sets the current model type for the LLMService.
   *
   * @param modelType the type of the model to be set for the LLMService
   * @return a Response object indicating the operation was successful
   * @throws IOException if an I/O error occurs during the operation
   */
  @POST
  @Path("/setModel/{modelType}")
  public Response setModel(@PathParam("modelType") LLMService.ModelType modelType) throws IOException {
    llmService.setModel(modelType);
    return Response.ok().build();
  }

}

