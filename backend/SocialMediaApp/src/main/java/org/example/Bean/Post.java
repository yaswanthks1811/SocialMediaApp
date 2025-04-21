package org.example.Bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Post implements Serializable {
    private int postId;
    private int userId;
    private String postCaption;
    private int postLikes;
    private String postType;
    private String filePath; 
    private Timestamp createdAt;

    public enum PostType {
        IMAGE, BLOG
    }

    public Post() {
        this.postLikes = 0;
    }

    public Post(int userId, String postCaption, String postType) {
        this();
        this.userId = userId;
        this.postCaption = postCaption;
        this.postType = postType;
    }

    public Post(int userId, int postId, String postCaption, String postType, Timestamp createdAt) {
        this();
        this.postId = postId;
        this.userId = userId;
        this.postCaption = postCaption;
        this.postType = postType;
        this.createdAt = createdAt;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPostCaption() {
        return postCaption;
    }

    public void setPostCaption(String postCaption) {
        this.postCaption = postCaption;
    }

    public int getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(int postLikes) {
        if (postLikes < 0) {
            throw new IllegalArgumentException("Post likes cannot be negative");
        }
        this.postLikes = postLikes;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        if (postType == null) {
            throw new IllegalArgumentException("Post type cannot be null");
        }
        this.postType = postType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getFilePath() {  
        return filePath;
    }

    public void setFilePath(String filePath) {  
        this.filePath = filePath;
    }

    public void incrementLikes() {
        this.postLikes++;
    }

    public void decrementLikes() {
        if (this.postLikes > 0) {
            this.postLikes--;
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", caption='" + postCaption + '\'' +
                ", likes=" + postLikes +
                ", type=" + postType +
                ", filePath='" + filePath + '\'' +  
                ", createdAt=" + createdAt +
                '}';
    }
}
