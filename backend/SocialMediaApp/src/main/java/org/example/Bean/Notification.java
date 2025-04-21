package org.example.Bean;

import java.sql.Timestamp;

public class Notification {

    private int notificationId;
    private int recipientUserId;
    private int senderUserId;
    private String senderUsername;
    private int postId;
    private String type; // 'follow', 'like', 'comment'
    private Timestamp createdAt;
    private boolean isRead;

    // Constructors
    public Notification() {}

    public Notification(int notificationId, int recipientUserId, int senderUserId, int postId,
                        String type, Timestamp createdAt, boolean isRead,String senderUsername) {
        this.notificationId = notificationId;
        this.recipientUserId = recipientUserId;
        this.senderUserId = senderUserId;
        this.postId = postId;
        this.type = type;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.senderUsername=senderUsername;
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(int recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    public int getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(int senderUserId) {
        this.senderUserId = senderUserId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    public String getSenderUsername(){
    	return senderUsername;
    }
    public void setSenderUsername(String senderUsername){
    	this.senderUsername = senderUsername;
    }

    // Builder
    public static class Builder {
        private final Notification notification;

        public Builder() {
            notification = new Notification();
        }

        public Builder setNotificationId(int id) {
            notification.setNotificationId(id);
            return this;
        }

        public Builder setRecipientUserId(int id) {
            notification.setRecipientUserId(id);
            return this;
        }

        public Builder setSenderUserId(int id) {
            notification.setSenderUserId(id);
            return this;
        }

        public Builder setPostId(int id) {
            notification.setPostId(id);
            return this;
        }

        public Builder setType(String type) {
            notification.setType(type);
            return this;
        }

        public Builder setCreatedAt(Timestamp timestamp) {
            notification.setCreatedAt(timestamp);
            return this;
        }

        public Builder setIsRead(boolean isRead) {
            notification.setRead(isRead);
            return this;
        }
        public Builder setSenderUsername(String senderUsername){
        	notification.setSenderUsername(senderUsername);
        	return this;
        }

        public Notification build() {
            return notification;
        }
        
        
    }
}
