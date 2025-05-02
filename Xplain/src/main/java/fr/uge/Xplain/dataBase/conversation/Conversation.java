package fr.uge.Xplain.dataBase.conversation;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.smallrye.common.annotation.RunOnVirtualThread;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@RunOnVirtualThread
public record Conversation(
       @ColumnName("id")long id,
       @ColumnName("title")String title,
       @ColumnName("description") String description,
       @ColumnName("createdAt") LocalDateTime createdAt
) {

 @JsonCreator
 @ConstructorProperties({"id", "title",  "description", "createdAt"})
 public Conversation {
    if (id < 0) {
       throw new IllegalArgumentException("Why the id of conversation is null ?");
    }
 }
}