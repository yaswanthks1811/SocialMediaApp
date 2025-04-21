package org.example.DAO.Comments;

import org.example.Bean.Comment;

import java.util.List;

public interface CommentsDAO {
    boolean addComments(Comment comment) throws CommentException;

    List<Comment> getCommentByPostId(int post_id) throws CommentException;

    Boolean updateComment(String comment, int user_id, int post_id) throws CommentException;

    Boolean deleteComment(int user_id, int post_id) throws CommentException;

}
