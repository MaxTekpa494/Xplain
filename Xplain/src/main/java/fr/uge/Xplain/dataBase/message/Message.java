package fr.uge.Xplain.dataBase.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.smallrye.common.annotation.RunOnVirtualThread;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

//@RegisterForReflection
@RunOnVirtualThread
public record Message(
   @ColumnName("id") long id,
   @ColumnName("conversationId") long conversationId,
   @ColumnName("sender")  String sender,
   @ColumnName("content") String content,
   @ColumnName("messageType") String messageType,
   @ColumnName("createdAt") LocalDateTime createdAt,
   @ColumnName("errorsCompiler") String errorsCompiler,
   @ColumnName("responseLLM") String responseLLM
) {


 @JsonCreator
 @ConstructorProperties({"id", "conversationId", "sender", "content", "messageType", "createdAt", "errorsCompiler", "responseLLM"})
 public Message {
   if (id < 0) {
     throw new IllegalArgumentException("Why the id of message is null ?");
   }
 }
}