package org.example.DAO.Followers;

import org.example.Bean.User;
import org.example.Utilities.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowersDaoImpl implements FollowersDAO{

    @Override
    public boolean followUser(int follower_id, int followed_id) throws FollowerException {
    	String query = "INSERT INTO FOLLOWERS(USER_ID, FOLLOWER_ID) VALUES(?, ?)";
    	try (Connection connection = DatabaseConnection.getConnection();
    	     PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    	    preparedStatement.setInt(1, followed_id);
    	    preparedStatement.setInt(2, follower_id);
    	    int rowsInserted = preparedStatement.executeUpdate();
    	    return rowsInserted > 0;
    	} catch (SQLException e) {
    	    throw new FollowerException("Cannot Follow: " + e.getMessage());
    	}

    }

    @Override
    public boolean unFollowUser(int follower_id, int followed_id) throws FollowerException {
        String query="DELETE FROM FOLLOWERS  WHERE USER_ID= ? AND FOLLOWER_ID=?" ;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, followed_id);
            preparedStatement.setInt(2, follower_id);
            int rowsInserted = preparedStatement.executeUpdate();
    	    return rowsInserted > 0;
        } catch (SQLException e) {
            throw new FollowerException("Cannot UnFollow");
        }
       

    }
    @Override
    public List<User> getFollowers(int user_id) {
        List<User> followers=new ArrayList<>();
        String query="SELECT U.* FROM USERS U " +
                "JOIN FOLLOWERS F ON U.USER_ID=F.FOLLOWER_ID " +
                "WHERE F.USER_ID=? ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user_id);
            ResultSet rs= preparedStatement.executeQuery();
            while (rs.next()){
            	User user = new User.Builder()
            			.userId(rs.getInt("USER_ID"))
                        .username(rs.getString("USERNAME"))
                        .firstName(rs.getString("FIRST_NAME"))
                        .lastName(rs.getString("LAST_NAME"))
                        .email(rs.getString("EMAIL"))
                        .bio(rs.getString("BIO")).build();
                followers.add(user);
            }
            return followers;
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getFollowing(int user_id) {
            List<User> following=new ArrayList<>();
            String query="SELECT U.* FROM USERS U " +
                    "JOIN FOLLOWERS F ON U.USER_ID=F.USER_ID " +
                    "WHERE F.FOLLOWER_ID=? ";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, user_id);
                ResultSet rs= preparedStatement.executeQuery();
                while (rs.next()){
                	User user = new User.Builder()
                			.userId(rs.getInt("USER_ID"))
                            .username(rs.getString("USERNAME"))
                            .firstName(rs.getString("FIRST_NAME"))
                            .lastName(rs.getString("LAST_NAME"))
                            .email(rs.getString("EMAIL"))
                            .bio(rs.getString("BIO")).build();
                    following.add(user);
                }
                return following;
    } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public boolean isFollowing(int follower_id, int following_id) {
        String query="SELECT USER_ID FROM FOLLOWERS WHERE USER_ID=? AND FOLLOWER_ID=? ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, following_id);
            preparedStatement.setInt(2, follower_id);
            ResultSet rs= preparedStatement.executeQuery();
            if (rs.next()){
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int followerCount(int user_id){
    	String query = "SELECT COUNT(*) AS F_COUNT FROM FOLLOWERS WHERE USER_ID =?";
    	try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    		preparedStatement.setInt(1, user_id);
    		ResultSet rs = preparedStatement.executeQuery();
    		if(rs.next()){
    			return rs.getInt("F_COUNT");
    		}
    	} catch (SQLException e){
    		e.printStackTrace();
    	}
    	return 0;
    }
    @Override
    public int followingCount(int user_id){
    	String query = "SELECT COUNT(*) AS F_COUNT FROM FOLLOWERS WHERE FOLLOWER_ID =?";
    	try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    		preparedStatement.setInt(1, user_id);
    		ResultSet rs = preparedStatement.executeQuery();
    		if(rs.next()){
    			return rs.getInt("F_COUNT");
    		}
    	} catch (SQLException e){
    		e.printStackTrace();
    	}
    	return 0;
    }
}

