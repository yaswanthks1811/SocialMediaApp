package org.example.DAO.Posts;

import org.example.Bean.Post;
import org.example.DAO.Comments.CommentException;

import com.example.DTO.PostDTO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface PostsDAO {
	Boolean createPost(Post post) throws PostException;

    Post getPostById(int post_id) throws SQLException;

    List<Post> getPostByUser(int user_id);

    List<Post> getFeedForUser(int user_id) throws SQLException;

    Boolean deletePost(int post_id) throws SQLException;

    List<Post> getPostListOfUser(int userId) throws CommentException;

    List<PostDTO> getTrendingPosts() throws CommentException;

    List<Post> getAllPosts();

}
