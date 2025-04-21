package org.example.service;

import org.example.Bean.Post;
import org.example.DAO.Posts.PostDaoImpl;
import org.example.DAO.Posts.PostException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class PostService {
    PostDaoImpl postDao=new PostDaoImpl();
    public List<Post> getUserPostByUserId(int user_id){
        return postDao.getPostListOfUser(user_id);
    }

    public boolean createPost(int user_id,String caption, String type,String file_path) throws PostException {
        Post post = new Post(user_id,caption,type);
        post.setFilePath(file_path);
        return postDao.createPost(post);
    }
    public boolean deletePost(int post_id) throws SQLException {
        return postDao.deletePost(post_id);
    }
    public List<Post> viewFeed(int user_id) throws SQLException {
        return postDao.getFeedForUser(user_id);
    }

    // Get a post by its ID
    public Post getPostById(int postId) throws SQLException {
        return postDao.getPostById(postId);
    }

    // Get all posts of a user
    public List<Post> getPostsByUserId(int userId) {
        return postDao.getPostByUser(userId);
    }

    public List<Post> getPostListByUserId(int userId) {
        return postDao.getPostListOfUser(userId);
    }
    
    public List<Post> getAllPosts(){
    	return postDao.getAllPosts();
    }

   
}
