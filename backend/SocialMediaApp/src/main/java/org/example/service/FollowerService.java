package org.example.service;

import org.example.Bean.User;
import org.example.DAO.Followers.FollowerException;
import org.example.DAO.Followers.FollowersDaoImpl;

import java.util.List;

public class FollowerService {
    FollowersDaoImpl followersDao=new FollowersDaoImpl();
    public List<User> seeFollowers(int user_id){
        return followersDao.getFollowers(user_id);
    }
    public List<User> seeFollowing(int user_id){
        return followersDao.getFollowing(user_id);
    }

    public boolean followUser(int followerId, int followedId) throws FollowerException {
        if (!followersDao.isFollowing(followerId,followedId)) {
            return followersDao.followUser(followerId, followedId);
        }else {
            System.out.println("Already Following this user");
            return false;
        }
    }

    public boolean unFollowUser(int followerId, int followedId) throws FollowerException {
        if (followersDao.isFollowing(followerId,followedId)) {
            return followersDao.unFollowUser(followerId,followedId);
        }else {
            System.out.println("User is not followed already!");
            return false;
        }
    }
    public boolean isFollowing(int userId,int follower_id){
    	return followersDao.isFollowing(follower_id, userId);
    }
    
    public int followerCount(int userId){
    	return followersDao.followerCount(userId);
    }
    public int followingCount(int userId){
    	return followersDao.followingCount(userId);
    }
    
}
    
