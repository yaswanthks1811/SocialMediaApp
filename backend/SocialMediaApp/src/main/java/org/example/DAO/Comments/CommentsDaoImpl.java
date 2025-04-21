package org.example.DAO.Comments;

import org.example.Bean.Comment;
import org.example.Utilities.DatabaseConnection;
import org.example.service.UserService;

import com.example.DTO.CommentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentsDaoImpl implements CommentsDAO {

    @Override
    public boolean addComments(Comment comment) throws CommentException {
        String query = "INSERT INTO COMMENTS (AUTHOR_USER_ID,COMMENT,POST_ID) " +
                "VALUES (?, ?, ?) RETURNING TRUE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, comment.getAuthorUserId());
            preparedStatement.setString(2, comment.getComment());
            preparedStatement.setInt(3, comment.getPostId());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CommentException("Cannot add Comment!");
        }
        return false;
    }

    @Override
    public List<Comment> getCommentByPostId(int post_id) throws CommentException {
        List<Comment> commentList=new ArrayList<>();
        String query = "SELECT * FROM COMMENTS WHERE POST_ID= ? ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Comment comment=new Comment(rs.getInt("POST_ID"),
                        rs.getInt("AUTHOR_USER_ID"),
                        rs.getString("COMMENT"),
                        rs.getTimestamp("CREATED_AT"));
                commentList.add(comment);

            }
            return commentList;
        } catch (SQLException e) {
            throw new CommentException("Cannot add Comment!");
        }
    }

    @Override
    public Boolean updateComment(String comment, int user_id, int post_id) throws CommentException {
        String query="UPDATE COMMENTS SET COMMENT = ? WHERE POST_ID= ? AND AUTHOR_USER_ID= ? RETURNING TRUE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment);
            preparedStatement.setInt(2, post_id);
            preparedStatement.setInt(3, user_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new CommentException("Cannot update Comment!");
        }
        return false;
    }

    @Override
    public Boolean deleteComment(int user_id, int post_id) throws CommentException {
        String query="DELETE FROM COMMENTS WHERE POST_ID= ? AND AUTHOR_USER_ID= ? RETURNING TRUE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post_id);
            preparedStatement.setInt(2, user_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new CommentException("Cannot update Comment!");
        }
        return false;
    }

	public List<CommentDTO> getCommentDTOByPostId(int post_id) throws CommentException {
		// TODO Auto-generated method stub
		UserService userService = new UserService();
		List<CommentDTO> commentList=new ArrayList<>();
        String query = "SELECT * FROM COMMENTS WHERE POST_ID= ? ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
            	CommentDTO commentDTO =new CommentDTO();
                Comment comment=new Comment(rs.getInt("POST_ID"),
                        rs.getInt("AUTHOR_USER_ID"),
                        rs.getString("COMMENT"),
                        rs.getTimestamp("CREATED_AT"));
                commentDTO.setComment(comment);
                commentDTO.setAuthorUsername(userService.getUsernameByUserId(comment.getAuthorUserId()));
                commentList.add(commentDTO);

            }
            return commentList;
        } catch (SQLException e) {
            throw new CommentException("Cannot add Comment!");
        }
	}
}
