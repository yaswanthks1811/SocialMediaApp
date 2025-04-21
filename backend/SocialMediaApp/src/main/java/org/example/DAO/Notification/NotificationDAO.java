package org.example.DAO.Notification;

import java.util.List;

import org.example.Bean.Notification;

public interface NotificationDAO {
    void addNotification(Notification notification);

    // Retrieves all unread notifications for a specific user (latest first)
    List<Notification> getNotificationsByUserId(int recipientUserId);

    // Marks a specific notification as read
    void markAsRead(int notificationId);

    // Marks all notifications as read for a specific user
    void markAllAsReadForUser(int recipientUserId);


}
