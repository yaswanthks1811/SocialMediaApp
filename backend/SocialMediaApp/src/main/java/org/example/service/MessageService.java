package org.example.service;

import org.example.Bean.Message;
import org.example.Bean.User;
import org.example.DAO.Message.MessageDaoImpl;

import com.example.DTO.MessageDTO;

import java.sql.SQLException;
import java.util.List;

public class MessageService {
    MessageDaoImpl messageDao = new MessageDaoImpl();
    public boolean sendMessage(int sender_id, int receiver_id, String message) throws SQLException {
        System.out.println(sender_id+" "+receiver_id+" "+message);
        Message message1=new Message.Builder().setSenderId(sender_id).setReceiverId(receiver_id).setMessageContent(message).build();
        return messageDao.sendMessage(message1);
    }

    public List<Message> getMessages(int receiver_id) throws SQLException {
        return messageDao.getMessages(receiver_id);
    }

    public List<Message> getAllMessages(int receiver_id) throws SQLException {
        return messageDao.getAllMessages(receiver_id);
    }

    public Message getMessage(int message_id) throws SQLException {
        return messageDao.getMessageById(message_id);
    }

    public boolean deleteMessage(int message_id) throws SQLException {
        return messageDao.deleteMessage(new Message.Builder().setMessageId(message_id).build());
    }
    public List<User> getSenders(int user_id){
    	return messageDao.getSenders(user_id);
    }
    public List<MessageDTO> getAllMessagesBySender(int userId,int senderId) throws SQLException{
    	return messageDao.getAllMessagesBySender(userId, senderId);
    }
    
}
