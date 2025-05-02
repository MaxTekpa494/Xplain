package fr.uge.Xplain.dataBase.conversation;

import fr.uge.Xplain.dataBase.message.Message;
import io.smallrye.common.annotation.RunOnVirtualThread;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

//@RegisterForReflection
@RegisterRowMapper(ConversationMapper.class)
@RunOnVirtualThread
public interface ConversationDao {
   /**
    * Inserts a new conversation into the database with the specified title and returns its generated ID.
    *
    * @param title the title of the new conversation
    * @return the unique identifier (ID) of the newly created conversation
    */
   @SqlUpdate("INSERT INTO conversations (title) VALUES (:title)")
   @GetGeneratedKeys
   long createConversation(@Bind("title") String title);

  /**
   * Retrieves a conversation from the database based on the provided conversation ID.
   *
   * @param id the unique identifier of the conversation to retrieve
   * @return the conversation object corresponding to the specified ID, or null if no conversation is found
   */
  @SqlQuery("SELECT * FROM conversations WHERE id = :id")
  Conversation getConversationById(@Bind("id") long id);

   /**
    * Retrieves a list of messages associated with a specific conversation ID, ordered by their creation timestamps.
    *
    * @param conversationId the unique identifier of the conversation whose messages should be fetched
    * @return a list of {@code Message} objects associated with the specified conversation ID
    */
   @SqlQuery("SELECT m.* FROM messages m " +
       "INNER JOIN conversations c ON m.conversationId = c.id " +
       "WHERE c.id = :conversationId " +
       "ORDER BY m.createdAt")
   List<Message> getMessagesByConversationId(@Bind("conversationId") long conversationId);


   /**
    * Retrieves a list of all conversations stored in the database.
    * The conversations are ordered by their creation timestamp in descending order.
    *
    * @return a list of {@code Conversation} objects, ordered by their creation date in descending order.
    *         Returns an empty list if no conversations are found.
    */
   @SqlQuery("SELECT * FROM conversations ORDER BY createdAt DESC")
   List<Conversation> getAllConversations();

  /**
   * Updates the title of a conversation in the database.
   *
   * @param id the unique identifier of the conversation to be updated
   * @param title the new title to set for the conversation
   */
  @SqlUpdate("UPDATE conversations SET title = :title WHERE id = :id")
  void updateConversationTitle(@Bind("id") long id, @Bind("title") String title);

  /**
   * Updates the "createdAt" timestamp of a conversation in the database to the current time.
   *
   * @param id the unique identifier of the conversation whose "createdAt" timestamp is to be updated
   */
  @SqlUpdate("UPDATE conversations SET CREATEDAT = CURRENT_TIMESTAMP WHERE id = :id")
  void updateConversationDate(@Bind("id") long id);

  @SqlUpdate("DELETE FROM conversations WHERE id = :id")
   void deleteConversation(@Bind("id") long id);

  @SqlUpdate("UPDATE conversations SET description = :description WHERE id = :id")
  void updateConversationDescription(@Bind("id") long id, @Bind("description") String description);
}
