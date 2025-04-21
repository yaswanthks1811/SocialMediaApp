import React, { useState } from 'react';
import { addComment, likePost, unlikePost } from '../services/api';
import { Link } from 'react-router-dom';
import './Post.css';
import { FaHeart, FaRegHeart, FaComment, FaRegComment } from 'react-icons/fa';
import he from 'he';

function Post({ post }) {
  const [liked, setLiked] = useState(post.liked);
  const [likeCount, setLikeCount] = useState(post.likeCount);
  const [commentCount, setCommentCount] = useState(post.comments.length || 0);
  const [showComments, setShowComments] = useState(false);
  const [newComment, setNewComment] = useState('');
  const [comments, setComments] = useState(post.comments || []);

  const handleLike = async () => {
    try {
      if (liked) {
        await likePost(post.postId);
        setLiked(false);
        setLikeCount(likeCount - 1);
      } else {
        await likePost(post.postId);
        setLiked(true);
        setLikeCount(likeCount + 1);
      }
    } catch (error) {
      console.error('Like/unlike error:', error);
    }
  };

  const handleCommentToggle = () => {
    setShowComments(!showComments);
  };

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (newComment.trim()) {
      const postId = e.target.elements.postId.value;
      const newCommentObject = {
        postId: postId,
        comment: newComment,
        createdAt: new Date().toISOString(),
      };
      setComments([...comments, newCommentObject]);
      setCommentCount(commentCount + 1);
      setNewComment('');
      try {
        await addComment(postId, newComment);
      } catch (error) {
        console.error('Error adding comment:', error);
      }
    }
  };

  return (
    <div className="post-container">
      <div className="post-header">
        <Link to={`/profile?userId=${post.userId}`} className="post-username">
          {post.username}
        </Link>
      </div>
      
      <div className="post-caption">{he.decode (post.caption)}</div>
      
      {post.mediaPath && (
        post.postType.toLowerCase() === 'image' ? (
          <img
            className="post-media"
            src={`http://localhost:8080/SocialMediaApp/${post.mediaPath}`}
            alt="Post Media"
          />
        ) : (
          <video
            className="post-video"
            src={`http://localhost:8080/SocialMediaApp/${post.mediaPath}`}
            controls
          />
        )
      )}
      
      <div className="post-actions">
        <button 
          className={`action-button ${liked ? 'liked' : ''}`}
          onClick={handleLike}
        >
          {liked ? <FaHeart /> : <FaRegHeart />}
          <span className="like-count">{likeCount}</span>
        </button>
        
        <button 
          className="action-button"
          onClick={handleCommentToggle}
        >
          {showComments ? <FaComment /> : <FaRegComment />}
          <span className="comment-count">{commentCount}</span>
        </button>
      </div>
      
      {showComments && (
        <div className="comments-section">
          {comments.map((comment, index) => (
            <div key={index} className="comment">
              <Link 
                to={`/profile?userId=${comment.comment.authorUserId}`} 
                className="comment-author"
              >
                {comment.authorUsername}
              </Link>
              <span className="comment-text">{(comment.comment.comment)}</span>
            </div>
          ))}
          
          <form onSubmit={handleCommentSubmit} className="comment-form">
            <input
              type="text"
              className="comment-input"
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              placeholder="Add a comment..."
            />
            <input type="hidden" name="postId" value={post.postId} />
            <button 
              type="submit" 
              className="comment-submit"
              disabled={!newComment.trim()}
            >
              Post
            </button>
          </form>
        </div>
      )}
    </div>
  );
}

export default Post;