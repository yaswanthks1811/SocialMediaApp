package org.example.DAO.Users;

import org.example.Bean.User;
import org.example.Utilities.DatabaseConnection;
import org.example.Utilities.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDaoImpl implements UsersDAO {
    public boolean createUser(User user) throws DataIntegrityException, SQLException {
        String query = "INSERT INTO Users (USERNAME, PASSWORD, EMAIL, ACCOUNT_STATUS) " +
                "VALUES (?, ?, ?, ?) RETURNING USER_ID";
        Connection connection= DatabaseConnection.getConnection();
       try(PreparedStatement preparedStatement=connection.prepareStatement(query)) {
           preparedStatement.setString(1, user.getUsername());
           preparedStatement.setString(2, user.getPassword());
           preparedStatement.setString(3, user.getEmail());
           preparedStatement.setString(4, user.getAccountStatus());
           ResultSet rs = preparedStatement.executeQuery();
           if (rs.next()) {
               return true;
           }
       }
       catch (SQLException e){
               throw new DataIntegrityException("Username Already Exists"+e);
       }

        return false;
    }
    
    public List<User> getRecommendedUsers(int currentUserId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id != ? AND user_id NOT IN " +
                     "(SELECT user_id FROM followers WHERE follower_id = ?) LIMIT 10";

        List<User> recommendations = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, currentUserId);
            ps.setInt(2, currentUserId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	User user = new User.Builder()
                        .username(rs.getString("USERNAME"))
                        .userId(rs.getInt("USER_ID"))
                        .firstName(rs.getString("FIRST_NAME"))
                        .lastName(rs.getString("LAST_NAME"))
                        .email(rs.getString("EMAIL"))
                        .bio(rs.getString("BIO")).build();
            	System.out.print(user.getUsername());
            	recommendations.add(user);
            }
        }

        return recommendations;
    }


    public boolean login(User user) throws DataIntegrityException, SQLException {

        String query = "SELECT USER_ID,PASSWORD,ACCOUNT_STATUS FROM USERS WHERE USERNAME=? ";
        Connection connection=DatabaseConnection.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement(query)){
            preparedStatement.setString(1,user.getUsername());
            ResultSet rs=preparedStatement.executeQuery();
            if ((rs.next())){
                if(PasswordUtils.checkPassword(user.getPassword(), rs.getString("PASSWORD"))
                		&&rs.getString("ACCOUNT_STATUS").equals("ACTIVE")) {
                    return true;
                }
            }
        }
        catch (SQLException e){
            throw new DataIntegrityException("Incorrect Username or Password!");
        }
        return false;

    }

    public User getUserByUsername(User user) throws SQLException {
        String query = "SELECT USER_ID,FIRST_NAME,LAST_NAME,BIO FROM USERS WHERE USERNAME= ?";
        User user1=new User();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1,user.getUsername());
             ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user1 = new User.Builder()
                        .username(user.getUsername())
                        .userId(rs.getInt("USER_ID"))
                        .firstName(rs.getString("FIRST_NAME"))
                        .lastName(rs.getString("LAST_NAME"))
                        .bio(rs.getString("BIO")).build();
            }
        }
        return user1;
    }
    public User getUserByUserId(User user) throws SQLException {
        String query = "SELECT * FROM USERS WHERE USER_ID= ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,user.getUserId());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = new User.Builder()
                		.userId(rs.getInt("USER_ID"))
                        .username(rs.getString("USERNAME"))
                        .firstName(rs.getString("FIRST_NAME"))
                        .lastName(rs.getString("LAST_NAME"))
                        .email(rs.getString("EMAIL"))
                        .bio(rs.getString("BIO")).build();
            }
        }
        return user;
    }

    @Override
    public boolean existsUser(String username) {
        String query="SELECT USER_ID FROM USERS WHERE USERNAME=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1,username);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    @Override
    public boolean existsEmail(String email) {
        String query="SELECT USER_ID FROM USERS WHERE EMAIL=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1,email);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    @Override
    public User getUserById(int user_id) {
        String query="SELECT * FROM USERS WHERE USER_ID=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,user_id);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return new User.Builder().userId(user_id).
                		userId(rs.getInt("USER_ID")).
                        username(rs.getString("USERNAME")).
                        firstName(rs.getString("FIRST_NAME")).
                        lastName(rs.getString("LAST_NAME")).
                        bio(rs.getString("BIO")).
                        accountStatus(rs.getString("ACCOUNT_STATUS")).
                        email(rs.getString("EMAIL")).build();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean updateUser(int userId, User user) {
        String sql = "UPDATE Users SET " +
                "FIRST_NAME = ?, " +
                "LAST_NAME = ?, " +
                "BIO = ?, " +
                "EMAIL = ? " +
                "WHERE USER_ID = ? ";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getBio());
            stmt.setString(4, user.getEmail());
            stmt.setInt(5, userId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
                return true;
            } else {
                System.out.println("User not found.");

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating user: " + e.getMessage());
        }
        return false;
    }


    @Override
    public boolean deleteUser(int user_id) throws SQLException {
        String query = "DELETE FROM USERS WHERE USER_ID=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user_id);
            int rowsDeleted = preparedStatement.executeUpdate(); 
            return rowsDeleted>0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM USERS order by user_id ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                User user = new User.Builder().
                		userId(rs.getInt("USER_ID")).
                        username(rs.getString("USERNAME")).
                        firstName(rs.getString("FIRST_NAME")).
                        lastName(rs.getString("LAST_NAME")).
                        bio(rs.getString("BIO")).
                        accountStatus(rs.getString("ACCOUNT_STATUS")).
                        email(rs.getString("EMAIL")).build();
                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public void updatePassword(int userId, String password) {
        String sql = "UPDATE Users SET " +
                "PASSWORD = ? " +
                "WHERE USER_ID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, password);
            stmt.setInt(2, userId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");

            } else {
                System.out.println("User not found.");

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating user: " + e.getMessage());
        }

    }
    public boolean banUser(int userId) throws SQLException {
        String query = "UPDATE USERS SET ACCOUNT_STATUS = ? WHERE USER_ID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,"BANNED");
            preparedStatement.setInt(2,userId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean unbanUser(int userId) throws SQLException {
        String query = "UPDATE USERS SET ACCOUNT_STATUS = ? WHERE USER_ID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,"ACTIVE");
            preparedStatement.setInt(2,userId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isBanned(int userId){
    	String query = "SELECT ACCOUNT_STATUS FROM USERS WHERE USER_ID= ?";
    	try (Connection connection = DatabaseConnection.getConnection();
    	        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    	            preparedStatement.setInt(1,userId);
    	            ResultSet rs = preparedStatement.executeQuery();
    	            if(rs.next()){
    	            	return "BANNED".equals(rs.getString("ACCOUNT_STATUS"));
    	            }
    	}
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int getUserIdByUsername(String username){
    	String query = "SELECT USER_ID FROM USERS WHERE USERNAME = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,username);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
            	return rs.getInt("USER_ID");
            }
    }
        catch(Exception e){
        	e.printStackTrace();
        }
        return 0;
    }
    
    public String getUsernameByUserId(int userId){
    	String query = "SELECT USERNAME FROM USERS WHERE USER_ID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1,userId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
            	return rs.getString("USERNAME");
            }
    }
        catch(Exception e){
        	e.printStackTrace();
        }
        return null;
    }
    
   


    public static class DataIntegrityException extends Throwable {
        public DataIntegrityException(String usernameAlreadyExists) {
            System.out.println(usernameAlreadyExists);
        }
    }




}
