package org.example.Bean;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private String messageContent;
    private int senderId;
    private int receiverId;
    private boolean isRead;
    private Timestamp createdAt;

    // Default constructor
    public Message() {
    }

    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Builder Class
    public static class Builder {
        private int messageId;
        private String messageContent;
        private int senderId;
        private int receiverId;
        private boolean isRead;
        private Timestamp createdAt;

        public Builder() {
        }

        public Builder setMessageId(int messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder setMessageContent(String messageContent) {
            this.messageContent = messageContent;
            return this;
        }

        public Builder setSenderId(int senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setReceiverId(int receiverId) {
            this.receiverId = receiverId;
            return this;
        }

        public Builder setRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public Builder setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.setMessageId(messageId);
            message.setMessageContent(messageContent);
            message.setSenderId(senderId);
            message.setReceiverId(receiverId);
            message.setRead(isRead);
            message.setCreatedAt(createdAt);
            return message;
        }
    }
}