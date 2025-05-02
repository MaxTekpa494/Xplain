package fr.uge.Xplain.dataBase.message;

import io.smallrye.common.annotation.RunOnVirtualThread;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@RunOnVirtualThread
public class MessageMapper implements RowMapper<Message> {
 @Override
 public Message map(ResultSet rs, StatementContext ctx) throws SQLException {
   return new Message(
     rs.getLong("id"),
     rs.getLong("conversationId"),
     rs.getString("sender"),
     rs.getString("content"),
     rs.getString("messageType"),
     rs.getTimestamp("createdAt").toLocalDateTime(),
     rs.getString("errorsCompiler"),
     rs.getString("responseLLM"));
 }
}