import React, { useState, useEffect } from 'react';
import { getTrendingPosts } from '../services/api'; // Adjust the path as needed
import Post from '../components/Post'; // Assuming you have a Post component
import Navbar from '../components/NavBar';
import { FaSpinner } from 'react-icons/fa';
import './Feed.css';

function Trending() {
  const [posts, setPosts] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getFeed = async () => {
      setLoading(true);
      const result = await getTrendingPosts();
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
    return <p>Error: {error}</p>;
  }

  return (
    
    <div className="feed-container">
      <div className="feed-content">
        
        <h2 className="feed-title" fafa >Trending</h2>
      {posts.map((post) => (
        <Post key={post.postId} post={post} />
      ))}
      </div>

        <Navbar className="bottom-navbar"  />
      
    </div>
  
  );
  
}

export default Trending;