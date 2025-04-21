
package org.example.DAO.Likes;

import org.example.Bean.Like;
import org.example.Bean.Post;
import org.example.DAO.Comments.CommentException;
import org.example.Utilities.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikesDaoImpl implements LikesDAO{


    @Override
    public Boolean addLike(Like like) throws LikeException {
        String query = "INSERT INTO LIKES (USER_ID, POST_ID) " +
                "VALUES (?, ?) RETURNING TRUE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, like.getUserId());
            preparedStatement.setInt(2, like.getPostId());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new LikeException("Cannot like this Post");
        }
        return false;
    }

    @Override
    public boolean removeLike(int user_id, int post_id) throws CommentException, LikeException {
        String query="DELETE FROM LIKES WHERE POST_ID= ? AND USER_ID= ? RETURNING TRUE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post_id);
            preparedStatement.setInt(2, user_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new LikeException("Cannot remove like in this Post");
        }
        return false;
    }

    @Override
    public int likeCount(int post_id) throws CommentException, LikeException {
        String query="SELECT COUNT(USER_ID) LIKE_COUNT FROM LIKES WHERE POST_ID= ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("LIKE_COUNT");
            }
        } catch (SQLException e) {
            throw new LikeException("Cannot fetch like count of this Post");
        }
        return 0;
    }

    @Override
    public List<String> getLikeForPost(int post_id) throws LikeException {
        List<String> likeList=new ArrayList<>();
        String query="SELECT U.USERNAME FROM USERS U " +
                "JOIN LIKES L ON U.USER_ID=L.USER_ID" +
                "WHERE L.POST_ID =? ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post_id);
            ResultSet rs= preparedStatement.executeQuery();
            while (rs.next()){
                likeList.add(rs.getString("USERNAME"));
            }
            return likeList;
        } catch (SQLException e) {
            throw new LikeException("Cannot fetch liked usernames of this Post");
        }
    }
    public boolean hasLiked(int user_id, int post_id) throws LikeException, SQLException {
        String query = "SELECT * FROM LIKES WHERE USER_ID= ? AND POST_ID= ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, post_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new LikeException("The user has not liked this Post");
        }
        return false;
    }

    public List<Integer> topLikedPostsId(){
        String query = "SELECT post_id, COUNT(*) AS like_count FROM likes GROUP BY post_id ORDER BY like_count DESC";
        List<Integer> likedPostsId=new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getResultSet();
            while (rs.next()) {
                likedPostsId.add(rs.getInt("post_id"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likedPostsId;
    }
}
