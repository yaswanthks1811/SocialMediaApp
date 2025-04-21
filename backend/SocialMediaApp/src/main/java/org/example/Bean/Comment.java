package org.example.Bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Comment implements Serializable {
    private int postId;
    private int authorUserId;
    private String comment;
    private Timestamp createdAt;


    public Comment(int postId, int authorUserId, String comment) {
        this.postId = postId;
        this.authorUserId = authorUserId;
        this.comment=comment;
    }


    public Comment(int postId, int authorUserId, String comment, Timestamp createdAt) {
        this.postId = postId;
        this.authorUserId = authorUserId;
        this.comment=comment;
        this.createdAt=createdAt;

    }

    public Comment(int postId) {
        this.postId=postId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(int authorUserId) {
        this.authorUserId = authorUserId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "postId=" + postId +
                ", authorUserId=" + authorUserId +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
