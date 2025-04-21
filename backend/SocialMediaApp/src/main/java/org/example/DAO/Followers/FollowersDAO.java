package org.example.DAO.Followers;

import java.util.List;

import org.example.Bean.User;

public interface FollowersDAO {
    boolean followUser(int follower_id, int followed_id) throws FollowerException;

    boolean unFollowUser(int follower_id, int followed_id) throws FollowerException;

    List<User> getFollowers(int user_id);

    List<User> getFollowing(int user_id);

    boolean isFollowing(int follower_id, int following_id);

	int followerCount(int user_id);

	int followingCount(int user_id);
}
