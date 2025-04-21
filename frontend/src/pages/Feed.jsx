import React, { useState, useEffect } from 'react';
import { fetchFeed } from '../services/api';
import Post from '../components/Post';
import Navbar from '../components/NavBar';
import './Feed.css'; // We'll create this CSS file
import { FaSpinner } from 'react-icons/fa';


function Feed() {
  const [posts, setPosts] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getFeed = async () => {
      setLoading(true);
      const result = await fetchFeed();
      if (result.success) {
        setPosts(result.data);
        setError(null);
      } else {
        setError(result.error);
        setPosts([]);
      }
      setLoading(false);
    };

    getFeed();
  }, []);

  if (loading) {
    return (
      <div className="loading-spinner">
                <FaSpinner className="spinner" />
       </div>
    );
  }

  if (error) {
    return (
      <div className="feed-container">
        <div className="error-message">Error: {error}</div>
      </div>
    );
  }

  return (
    <div className="feed-container">
      <div className="feed-content">
        
        <h2 className="feed-title" fafa >Feed</h2>
        {posts.map((post) => (
          <Post key={post.postId} post={post} />
        ))}
      </div>
      <Navbar className="bottom-navbar" />
    </div>
  );
}

export default Feed;