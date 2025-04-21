import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import Post from './Post';
import he from 'he';

const API_BASE_URL = 'http://localhost:8080/SocialMediaApp';

function DisplayPost() {
    const { postId } = useParams();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await axios.get(`${API_BASE_URL}/api/admin/post?postId=${postId}`, {
                    withCredentials: true,
                });

                setPost(response.data.post);
                setError(null);
            } catch (err) {
                console.error('Error fetching post:', err);
                setError('Failed to load post.');
            } finally {
                setLoading(false);
            }
        };

        fetchPost();
    }, [postId]);

    if (loading) return <p>Loading post...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div className="post-container">
              <div className="post-header">
                <Link to={`/profile?userId=${post.userId}`} className="post-username">
                  {post.username}
                </Link>
              </div>
              
              <div className="post-caption">{he.decode(post.caption)}</div>
              
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
              </div>
    );
}

export default DisplayPost;
