package org.example.DAO.Notification;

import org.example.Bean.Notification;
import org.example.Utilities.DatabaseConnection;
import org.example.service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDaoImpl implements NotificationDAO {

    // Add a new notification
    public void addNotification(Notification notification) {
        String sql = "INSERT INTO Notifications (recipient_user_id, sender_user_id, post_id, type, created_at, is_read) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notification.getRecipientUserId());
            stmt.setInt(2, notification.getSenderUserId());
            stmt.setInt(3, notification.getPostId());
            stmt.setString(4, notification.getType());
            stmt.setTimestamp(5, notification.getCreatedAt());
            stmt.setBoolean(6, notification.isRead());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all notifications for a user (latest first)
    public List<Notification> getNotificationsByUserId(int recipientUserId) {
        List<Notification> notifications = new ArrayList<>();
     UserService userService = new UserService();

        String sql = "SELECT * FROM Notifications WHERE recipient_user_id = ? AND is_read= false  ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recipientUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification.Builder()
                        .setNotificationId(rs.getInt("notification_id"))
                        .setRecipientUserId(rs.getInt("recipient_user_id"))
                        .setSenderUserId(rs.getInt("sender_user_id"))
                        .setPostId(rs.getInt("post_id"))
                        .setType(rs.getString("type"))
                        .setCreatedAt(rs.getTimestamp("created_at"))
                        .setIsRead(rs.getBoolean("is_read"))
                        .setSenderUsername(userService.getUsernameByUserId(rs.getInt("sender_user_id")))
                        .build();
                

                notifications.add(notification);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    // Mark a single notification as read
    public void markAsRead(int notificationId) {
        String sql = "UPDATE Notifications SET is_read = TRUE WHERE notification_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mark all notifications as read for a user
    public void markAllAsReadForUser(int recipientUserId) {
        String sql = "UPDATE Notifications SET is_read = TRUE WHERE recipient_user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recipientUserId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
