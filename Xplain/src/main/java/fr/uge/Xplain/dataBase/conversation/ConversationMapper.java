package fr.uge.Xplain.dataBase.conversation;

import io.smallrye.common.annotation.RunOnVirtualThread;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@RunOnVirtualThread
public class ConversationMapper implements RowMapper<Conversation> {

  @Override
  public Conversation map(ResultSet rs, StatementContext ctx) throws SQLException {
    try {

      return new Conversation(
              rs.getLong("id"),
              rs.getString("title"),
              rs.getString("description"),
              rs.getTimestamp("createdAt") != null
                      ? rs.getTimestamp("createdAt").toLocalDateTime()
                      : null
      );
    } catch (SQLException e) {
      System.err.println("Mapping error: " + e.getMessage());
      throw e;
    }
  }

}
