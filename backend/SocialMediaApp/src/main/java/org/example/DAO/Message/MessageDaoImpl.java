package org.example.DAO.Message;

import org.example.Bean.Message;
import org.example.Bean.User;
import org.example.DAO.Users.UsersDaoImpl;
import org.example.Utilities.DatabaseConnection;
import org.example.service.UserService;

import com.example.DTO.MessageDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageDaoImpl implements MessageDao {
	UsersDaoImpl userDao = new UsersDaoImpl();

    @Override
    public boolean sendMessage(Message message) throws SQLException {
//        System.out.println(message.getSenderId()+" "+message.getMessageContent()+" "+message.getReceiverId());

        String query = "INSERT INTO MESSAGES (SENDER_ID,RECEIVER_ID,MESSAGE) VALUES (?,?,?) ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, message.getSenderId());
            preparedStatement.setInt(2, message.getReceiverId());
            preparedStatement.setString(3, message.getMessageContent());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteMessage(Message message) throws SQLException {
        String query = "DELETE FROM MESSAGES WHERE MESSAGE_ID=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, message.getMessageId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Message getMessageById(int messageId) throws SQLException {
        String query = "SELECT * FROM MESSAGES WHERE MESSAGE_ID=?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, messageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Message message = new Message.Builder().setMessageContent(resultSet.getString("MESSAGE"))
                        .setReceiverId(resultSet.getInt("RECEIVER_ID"))
                        .setMessageId(resultSet.getInt("MESSAGE_ID"))
                        .setCreatedAt(resultSet.getTimestamp("CREATED_AT")).build();
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> getMessages(int user_id) throws SQLException {
        String query = "SELECT * FROM MESSAGES WHERE RECEIVER_ID=? AND SEEN=FALSE ORDER BY MESSAGE_ID DESC";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (resultSet.next()) {
                Message message = new Message.Builder().setMessageContent(resultSet.getString("MESSAGE"))
                        .setReceiverId(resultSet.getInt("SENDER_ID"))
                        .setMessageId(resultSet.getInt("MESSAGE_ID"))
                        .setCreatedAt(resultSet.getTimestamp("CREATED_AT")).build();
                messages.add(message);

            }
            return messages;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Message> getAllMessages(int user_id) throws SQLException {
        String query = "SELECT * FROM MESSAGES WHERE RECEIVER_ID=? ORDER BY MESSAGE_ID DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (resultSet.next()) {
                Message message = new Message.Builder().setMessageContent(resultSet.getString("MESSAGE"))
                        .setReceiverId(resultSet.getInt("SENDER_ID"))
                        .setMessageId(resultSet.getInt("MESSAGE_ID"))
                        .setCreatedAt(resultSet.getTimestamp("CREATED_AT")).build();
                messages.add(message);

            }
            return messages;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isRead(int messageId) throws SQLException {
        String query = "SELECT SEEN FROM MESSAGES WHERE MESSAGE_ID=?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, messageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("SEEN");
            }
        }
        return false;
    }

	public List<User> getSenders(int userId) {
		Set<Integer> chatPartnerIds = new HashSet<>(); // Use a Set to store unique user IDs
        List<User> chatPartners = new ArrayList<>();
        String query = "SELECT DISTINCT CASE WHEN sender_id = ? THEN receiver_id ELSE sender_id END AS chat_partner_id " +
                       "FROM messages WHERE sender_id = ? OR receiver_id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); // Use try-with-resources
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) { // Use try-with-resources for ResultSet
                while (rs.next()) {
                    int chatPartnerId = rs.getInt("chat_partner_id");
                    if (chatPartnerIds.add(chatPartnerId)) { // Add only if not already present
                        User user = userDao.getUserById(chatPartnerId);
                        if (user != null) { // Handle potential null user
                            chatPartners.add(user);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception properly
            return null; // Or throw an exception, depending on your error handling policy
        }

        return chatPartners;
	}
	public List<MessageDTO> getAllMessagesBySender(int user_id,int senderId) throws SQLException {
		UserService userService = new UserService();
        String query = "SELECT * FROM MESSAGES WHERE RECEIVER_ID=? AND SENDER_ID=? OR RECEIVER_ID=? AND SENDER_ID=? ORDER BY MESSAGE_ID ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, senderId);
            preparedStatement.setInt(3, senderId);
            preparedStatement.setInt(4, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<MessageDTO> messages = new ArrayList<>();
            while (resultSet.next()) {
            	MessageDTO messageDTO = new MessageDTO();
                Message message = new Message.Builder().setMessageContent(resultSet.getString("MESSAGE"))
                		.setSenderId(resultSet.getInt("RECEIVER_ID"))
                        .setReceiverId(resultSet.getInt("SENDER_ID"))
                        .setMessageId(resultSet.getInt("MESSAGE_ID"))
                        .setCreatedAt(resultSet.getTimestamp("CREATED_AT")).build();
                messageDTO.setMessage(message);
                messageDTO.setSenderUsername(userService.getUsernameByUserId(message.getReceiverId()));
                messages.add(messageDTO);
                System.out.print(message.getSenderId()+" "+message.getReceiverId());

            }
            return messages;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
