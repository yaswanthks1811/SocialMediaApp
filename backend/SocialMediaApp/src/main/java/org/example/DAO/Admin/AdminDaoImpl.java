package org.example.DAO.Admin;

import org.example.Bean.Admin;
import org.example.Utilities.DatabaseConnection;
import org.example.Utilities.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDaoImpl implements AdminDAO {
    @Override
    public Boolean authenticate(Admin admin) throws SQLException, IllegalArgumentException {
        if (admin == null || admin.getAdminUsername() == null || admin.getAdminPassword() == null) {
            throw new IllegalArgumentException("Admin credentials cannot be null");
        }

        String query = "SELECT ADMINUSER_ID, ADMIN_PASSWORD FROM ADMIN WHERE ADMIN_USERNAME=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, admin.getAdminUsername());
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("ADMIN_PASSWORD");

                if (storedHash == null || !storedHash.startsWith("$2a$")) {
                    throw new IllegalArgumentException("Invalid password hash format in database");
                }

                boolean passwordMatches = PasswordUtils.checkPassword(admin.getAdminPassword(), storedHash);

                if (passwordMatches) {
                    return rs.getInt("ADMINUSER_ID") != 0;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Authentication failed: " + e.getMessage());
        }
    }
    public Boolean addAdmin(Admin admin) throws AdminException, SQLException {
        String query = "INSERT INTO ADMIN(ADMIN_USERNAME,ADMIN_PASSWORD) " +
                "VALUES(?,?) RETURNING TRUE";
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, admin.getAdminUsername());
            preparedStatement.setString(2, admin.getAdminPassword());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AdminException("Cannot add Admin!");
        }
        return false;
    }

    public boolean changePassword(String adminUsername, String newPassword) throws SQLException, AdminException {
        String query = "UPDATE ADMIN SET ADMIN_PASSWORD =? " +
                "WHERE ADMIN_USERNAME=?";
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, adminUsername);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
                return true;
            } else {
                System.out.println("User not found.");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AdminException("Cannot add Admin!");
        }
        return false;
    }
    
    public Admin getAdminByUsername(String username) throws SQLException{
    	String query = "SELECT * FROM ADMIN WHERE ADMIN_USERNAME = ?";
    	Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
            Admin admin = new Admin.Builder().adminFirstName(rs.getString("FIRST_NAME"))
            		.adminLastName(rs.getString("LAST_NAME"))
            		.adminUserId(rs.getInt("ADMINUSER_ID"))
            		.adminUsername(username)
            		.email(rs.getString("EMAIL"))
            		.createdAt(rs.getTimestamp("CREATED_AT"))
            		.build();
            return admin;
            }
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return null;
        
    	
    }

}
