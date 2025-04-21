package org.example.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Follower implements Serializable {
    private int userId;          // User being followed
    private int followerId;      // User who is following
    private LocalDateTime followedAt;

    public Follower() {
        this.followedAt = LocalDateTime.now();
    }

    public Follower(int userId, int followerId) {
        this();
        this.userId = userId;
        this.followerId = followerId;
        validateRelationship();
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        validateRelationship();
    }

    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
        validateRelationship();
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
    }

    private void validateRelationship() {
        if (userId == followerId) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }
    }

    // Business methods
    public boolean isMutualFollow(Follower other) {
        return this.userId == other.followerId &&
                this.followerId == other.userId;
    }

    @Override
    public String toString() {
        return "Follower{" +
                "userId=" + userId +
                ", followerId=" + followerId +
                ", followedAt=" + followedAt +
                '}';
    }
}