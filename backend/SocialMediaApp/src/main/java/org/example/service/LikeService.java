package org.example.service;

import org.example.Bean.Like;
import org.example.Bean.Post;
import org.example.DAO.Comments.CommentException;
import org.example.DAO.Likes.LikeException;
import org.example.DAO.Likes.LikesDaoImpl;
import org.example.DAO.Posts.PostDaoImpl;
import org.example.DAO.Users.UsersDaoImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LikeService {
    LikesDaoImpl likesDao=new LikesDaoImpl();
    PostDaoImpl postDao=new PostDaoImpl();
    UsersDaoImpl userDao=new UsersDaoImpl();
    public Boolean likePost(int userId, int postId) throws LikeException, SQLException {
        if(likesDao.hasLiked(userId,postId)){
            return false;
        }
        Like like=new Like(userId,postId);
        return likesDao.addLike(like);
    }

    public int likeCount(int postId) throws CommentException, LikeException {
        return likesDao.likeCount(postId);
    }

    public List<Post> mostLikedPosts() throws SQLException {
        List<Post> likedPosts=new ArrayList<>();
        List<Integer> likedPostId = likesDao.topLikedPostsId();
        for(Integer postId : likedPostId){
            likedPosts.add(postDao.getPostById(postId));
        }
        return likedPosts;
    }
    public boolean removeLike(int user_id,int post_id) throws CommentException, LikeException{
    	return likesDao.removeLike(user_id, post_id);
    }
    
    public boolean hasLiked(int post_id,int user_id) throws SQLException, LikeException{
    	return likesDao.hasLiked(user_id, post_id);
    }
}
