package org.example.DAO.Message;

import org.example.Bean.Message;

import java.sql.SQLException;
import java.util.List;

public interface MessageDao {
    boolean sendMessage(Message message) throws SQLException;
    boolean deleteMessage(Message message) throws SQLException;
    Message getMessageById(int messageId) throws SQLException;

    List<Message> getMessages(int user_id) throws SQLException;

    List<Message> getAllMessages(int user_id) throws SQLException;
    boolean isRead(int messageId) throws SQLException;

}
