package org.example.DAO.Likes;

import org.example.Bean.Like;
import org.example.DAO.Comments.CommentException;

import java.sql.SQLException;
import java.util.List;

public interface LikesDAO {
    Boolean addLike(Like like) throws LikeException;

    boolean removeLike(int user_id, int post_id) throws LikeException, CommentException;

    int likeCount(int post_id) throws LikeException, CommentException;

    List<String> getLikeForPost(int post_id) throws LikeException;

    boolean hasLiked(int user_id, int post_id) throws LikeException, SQLException;

    List<Integer> topLikedPostsId();
}
