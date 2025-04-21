package org.example.DAO.Posts;

import org.example.Bean.Post;
import org.example.DAO.Comments.CommentException;
import org.example.DAO.Comments.CommentsDaoImpl;
import org.example.Utilities.DatabaseConnection;

import com.example.DTO.PostDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDaoImpl implements PostsDAO {
	CommentsDaoImpl commentsDao = new CommentsDaoImpl();

	@Override
	public Boolean createPost(Post post) throws PostException {
	    String query = "INSERT INTO POSTS (USER_ID, POST_TYPE, POST_CAPTION, FILE_PATH) " +
	            "VALUES (?, ?, ?, ?) RETURNING POST_ID";
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setInt(1, post.getUserId());
	        preparedStatement.setString(2, post.getPostType());
	        preparedStatement.setString(3, post.getPostCaption());
	        preparedStatement.setString(4, post.getFilePath()); // new
	        ResultSet rs = preparedStatement.executeQuery();
	        if (rs.next()) {
	            return true;
	        }
	    } catch (SQLException e) {
	    	e.printStackTrace();
	        
	            throw new PostException("Cannot create a post!");
	        
	    }
	    return false;
	}

	@Override
	public Post getPostById(int post_id) throws SQLException {
	    String query = "SELECT POST_ID,USER_ID,POST_TYPE,POST_CAPTION,FILE_PATH,CREATED_AT FROM POSTS WHERE POST_ID= ? ";
	    try(Connection connection=DatabaseConnection.getConnection();
	        PreparedStatement preparedStatement= connection.prepareStatement(query)){
	        preparedStatement.setInt(1,post_id);
	        ResultSet rs=preparedStatement.executeQuery();
	        if(rs.next()){
	            Post post= new Post(rs.getInt("USER_ID"),
	                    rs.getInt("POST_ID"),
	                    rs.getString("POST_CAPTION"),
	                    rs.getString("POST_TYPE"),
	                    rs.getTimestamp("CREATED_AT"));
	            post.setFilePath(rs.getString("FILE_PATH")); // new
	            return post;
	        }
	    }
	    return null;
	}
	

    @Override
    public List<Post> getPostByUser(int user_id) {
        List<Post> postList=new ArrayList<>();
        String query = "SELECT FILE_PATH,POST_ID,USER_ID,POST_TYPE,POST_CAPTION,CREATED_AT FROM POSTS WHERE USER_ID= ? ";
        try(Connection connection=DatabaseConnection.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement(query)){
            preparedStatement.setInt(1,user_id);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){
                Post post=new Post(rs.getInt("USER_ID"),
                        rs.getInt("POST_ID"),
                        rs.getString("POST_CAPTION"),
                        rs.getString("POST_TYPE"),
                        rs.getTimestamp("CREATED_AT"));
                post.setFilePath(rs.getString("FILE_PATH"));
                
                postList.add(post);
            }} catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return postList;
    }

    @Override
    public List<Post> getFeedForUser(int user_id) throws SQLException {
        List<Post> feeds=new ArrayList<>();
        String query = "SELECT P.FILE_PATH,F.USER_ID,U.USERNAME,P.POST_ID,P.POST_CAPTION,P.POST_TYPE,P.CREATED_AT FROM FOLLOWERS F\n" +
                "JOIN POSTS P ON F.USER_ID=P.USER_ID\n" +
                "JOIN USERS U ON P.USER_ID=U.USER_ID\n" +
                "WHERE F.FOLLOWER_ID= ? " +
                "ORDER BY P.CREATED_AT DESC";

        try(Connection connection=DatabaseConnection.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement(query);){
            preparedStatement.setInt(1,user_id);

            ResultSet rs=preparedStatement.executeQuery();

            while(rs.next()){
            	Post post = new Post(rs.getInt("USER_ID"),
            	        rs.getInt("POST_ID"),
            	        rs.getString("POST_CAPTION"),
            	        rs.getString("POST_TYPE"),
            	        (Timestamp) rs.getObject("CREATED_AT"));
            	post.setFilePath(rs.getString("FILE_PATH")); // Add this line
            	feeds.add(post);

            }
        }
        catch(Exception e){
        	System.out.println("Exception in feed dao");
        	e.printStackTrace();
        }
        return feeds;
    }

    @Override
    public Boolean deletePost(int post_id) throws SQLException {
        String query = "DELETE FROM POSTS WHERE POST_ID= ? ";
        try(Connection connection=DatabaseConnection.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement(query)){
            preparedStatement.setInt(1,post_id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Post> getPostListOfUser(int userId) {
        List<Post> postList = new ArrayList<>();
        String sql = "SELECT * FROM Posts WHERE USER_ID = ? ORDER BY CREATED_AT DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
            	Post post = new Post(rs.getInt("USER_ID"),
            	        rs.getInt("POST_ID"),
            	        rs.getString("POST_CAPTION"),
            	        rs.getString("POST_TYPE"),
            	        (Timestamp) rs.getObject("CREATED_AT"));
            	post.setFilePath(rs.getString("FILE_PATH")); // Add this line
            	postList.add(post);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postList;
    }
    public List<PostDTO> getTrendingPosts() throws CommentException {
        List<PostDTO> trendingPosts = new ArrayList<>();
        String query = "SELECT p.*, u.username, " +
                       "(SELECT COUNT(*) FROM Likes l WHERE l.post_id = p.post_id) as likeCount, " +
                       "(SELECT COUNT(*) FROM Comments c WHERE c.post_id = p.post_id) as commentCount " +
                       "FROM Posts p " +
                       "JOIN Users u ON p.user_id = u.user_id " +
                       "ORDER BY ( " +
                       "    (SELECT COUNT(*) FROM Likes l WHERE l.post_id = p.post_id) * 2 + " +
                       "    (SELECT COUNT(*) FROM Comments c WHERE c.post_id = p.post_id) " +
                       ") DESC " +
                       "LIMIT 10"; // Top 10 trending

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PostDTO post = new PostDTO();
                int post_id = rs.getInt("POST_ID");
                post.setUsername(rs.getString("USERNAME"));
                post.setComments(commentsDao.getCommentDTOByPostId(post_id));
                post.setLikeCount(rs.getInt("LIKECOUNT"));
                post.setMediaPath(rs.getString("FILE_PATH"));
                post.setDateTime(rs.getTimestamp("CREATED_AT"));
                post.setPostType(rs.getString("POST_TYPE"));
                post.setPostId(post_id);
                post.setCaption(rs.getString("POST_CAPTION"));
                
                trendingPosts.add(post);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingPosts;
    }

	public List<Post> getAllPosts() {
		List<Post> postList = new ArrayList<>();
        String sql = "SELECT * FROM Posts ORDER BY CREATED_AT DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
            	Post post = new Post(rs.getInt("USER_ID"),
            	        rs.getInt("POST_ID"),
            	        rs.getString("POST_CAPTION"),
            	        rs.getString("POST_TYPE"),
            	        (Timestamp) rs.getObject("CREATED_AT"));
            	post.setFilePath(rs.getString("FILE_PATH")); 
            	postList.add(post);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postList;
		
	}
    
   

    

}
