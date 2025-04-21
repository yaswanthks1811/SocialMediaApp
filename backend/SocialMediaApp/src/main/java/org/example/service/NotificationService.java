package org.example.service;

import org.example.Bean.Notification;
import org.example.DAO.Notification.NotificationDaoImpl;

import java.util.List;

public class NotificationService {

    private NotificationDaoImpl notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDaoImpl();
    }

    // Create a new notification
    public void createNotification(int recipientUserId, int senderUserId, int postId, String type) {
        Notification notification = new Notification.Builder()
                .setRecipientUserId(recipientUserId)
                .setSenderUserId(senderUserId)
                .setPostId(postId)
                .setType(type)
                .build();

        notificationDAO.addNotification(notification);
    }

    // Get notifications for a specific user
    public List<Notification> getNotificationsForUser(int recipientUserId) {
        return notificationDAO.getNotificationsByUserId(recipientUserId);
    }

    // Mark one notification as read
    public void markNotificationAsRead(int notificationId) {
        notificationDAO.markAsRead(notificationId);
    }

    // Mark all notifications as read for a user
    public void markAllNotificationsAsRead(int recipientUserId) {
        notificationDAO.markAllAsReadForUser(recipientUserId);
    }
}
