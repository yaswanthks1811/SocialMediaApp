import React, { useState, useEffect } from 'react';
import { followUser, getRecommendations, toggleFollow } from '../services/api';
import { Link } from 'react-router-dom';
import { FaUser, FaSpinner, FaUserPlus, FaUserCheck } from 'react-icons/fa';
import './Recommendations.css';
import Navbar from '../components/NavBar';

function Recommendations() {
  const [recommendations, setRecommendations] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRecommendations = async () => {
      setLoading(true);
      try {
        const result = await getRecommendations();
        if (result.success) {
          setRecommendations(result.data);
          setError(null);
        } else {
          setError(result.error);
          setRecommendations([]);
        }
      } catch (err) {
        console.error('Error fetching recommendations:', err);
        setError('Failed to load recommendations');
        setRecommendations([]);
      }
      setLoading(false);
    };

    fetchRecommendations();
  }, []);

  const handleFollowToggle = async (userId, isFollowing) => {
    try {
      const action = isFollowing ? 'unfollow' : 'follow';
      const result = await followUser(userId);
      if (result.success) {
        setRecommendations(prev => prev.map(user => 
          user.userId === userId ? { ...user, isFollowing: !isFollowing } : user
        ));
      } else {
        setError(result.error);
      }
    } catch (err) {
      console.error('Follow toggle error:', err);
      setError('Failed to toggle follow');
    }
  };
  

  if (loading) {
    return (
          <div className="loading-spinner">
                    <FaSpinner className="spinner" />
           </div>
        );
  }

  if (error) {
    return (
      <div className="recommendations-error">
        <p>Error: {error}</p>
      </div>
    );
  }

  return (
    <div className="recommendations-container">
      <h2 className="recommendations-header">
        <FaUser className="header-icon" /> Suggestions For You
      </h2>
      
      {recommendations.length > 0 ? (
        <ul className="recommendations-list">
          {recommendations.map((user) => (
            <li key={user.userId} className="recommendation-item">
              <Link to={`/profile?userId=${user.userId}`} className="user-link">
                <div className="user-avatar">
                  {user.avatar ? (
                    <img src={user.avatar} alt={user.username} />
                  ) : (
                    <FaUser className="default-avatar" />
                  )}
                </div>
                <div className="user-info">
                  <span className="username">{user.username}</span>
                  {user.fullName && <span className="full-name">{user.fullName}</span>}
                  <span className="follow-status">
                    {user.isFollowing ? 'Following' : 'Suggested for you'}
                  </span>
                </div>
              </Link>
              <button
                className={`follow-button ${user.isFollowing ? 'following' : ''}`}
                onClick={() => handleFollowToggle(user.userId, user.isFollowing)}
              >
                {user.isFollowing ? (
                  <>
                    <FaUserCheck /> Following
                  </>
                ) : (
                  <>
                    <FaUserPlus /> Follow
                  </>
                )}
              </button>
            </li>
          ))}
        </ul>
      ) : (
        <div className="no-recommendations">
          <p>No recommendations found.</p>
        </div>
      )}
      <Navbar className="bottom-navbar" type='2' />
    </div>
    
  );
}

export default Recommendations;