package org.example.service;

import org.example.Bean.Comment;
import org.example.DAO.Comments.CommentException;
import org.example.DAO.Comments.CommentsDaoImpl;

import com.example.DTO.CommentDTO;

import java.util.List;

public class CommentService {
    CommentsDaoImpl commentsDao=new CommentsDaoImpl();
    public boolean commentPost(int userId, int postId ,String commentText ) throws CommentException {
        Comment comment = new Comment(postId,userId,commentText);
        return commentsDao.addComments(comment);
    }

    public List<Comment> getComments(int postId) throws CommentException {
        return commentsDao.getCommentByPostId(postId);

    }
    public List<CommentDTO> getCommentsDTO(int postId) throws CommentException{
        return commentsDao.getCommentDTOByPostId(postId);

    }
}
