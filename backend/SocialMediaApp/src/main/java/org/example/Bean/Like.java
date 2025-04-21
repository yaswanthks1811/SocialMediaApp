package org.example.Bean;


import java.io.Serializable;
import java.time.LocalDateTime;

public class Like implements Serializable {
    private int likeId;
    private int userId;
    private int postId;
    private int commentId;
    private LocalDateTime likedAt;

    // Constructors


    // Constructor for post likes
    public Like(int userId, int postId) {

        this.userId = userId;
        this.postId = postId;
    }

    // Constructor for comment likes
    public Like(int userId, int commentId, boolean isCommentLike) {
        this.userId = userId;
        this.commentId = commentId;
    }

    // Getters and Setters
    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getPostId() {
        return postId;
    }


    public Integer getCommentId() {
        return commentId;
    }


    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(LocalDateTime likedAt) {
        this.likedAt = likedAt;
    }


    @Override
    public String toString() {
        return "Like{" +
                "likeId=" + likeId +
                ", userId=" + userId +
                ", postId=" + postId +
                ", commentId=" + commentId +
                ", likedAt=" + likedAt +
                '}';
    }
}
