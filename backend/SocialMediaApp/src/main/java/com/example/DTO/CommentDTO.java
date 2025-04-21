package com.example.DTO;

import org.example.Bean.Comment;

public class CommentDTO {
	public String getAuthorUsername() {
		return authorUsername;
	}
	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}
	private String authorUsername;
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	private Comment comment;
	
}
