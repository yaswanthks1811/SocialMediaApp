import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { viewFollowers } from '../services/api';
import { FaUserFriends, FaUserCircle, FaSpinner } from 'react-icons/fa';
import './ViewFollowers.css';

function ViewFollowers() {
    const { userId } = useParams();
    const [followers, setFollowers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const fetchFollowers = async () => {
            setLoading(true);
            try {
                const response = await viewFollowers(userId);
                if (response.success) {
                    setFollowers(response.data.users);
                    setError(null);
                } else {
                    setError(response.error || 'Failed to load followers');
                }
            } catch (error) {
                console.error('Error fetching followers:', error);
                setError('Failed to load followers.');
            }
            setLoading(false);
        };

        fetchFollowers();
    }, [userId]);

    const filteredFollowers = followers.filter(follower =>
        follower.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
        (follower.fullName && follower.fullName.toLowerCase().includes(searchTerm.toLowerCase()))
    );

    if (loading) {
        return (
            <div className="followers-container">
                <div className="loading-spinner">
                    <FaSpinner className="spinner-icon" />
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="followers-container">
                <div className="error-message">
                    <FaUserFriends className="error-icon" />
                    <p>{error}</p>
                </div>
            </div>
        );
    }

    return (
        <div className="followers-container">
            <div className="followers-header">
                <h2><FaUserFriends className="header-icon" /> Followers</h2>
                
                <div className="search-bar">
                    <FaUserCircle className="search-icon" />
                    <input
                        type="text"
                        placeholder="Search followers..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
            </div>

            {filteredFollowers.length > 0 ? (
                <ul className="followers-list">
                    {filteredFollowers.map(follower => (
                        <li key={follower.userId} className="follower-item">
                            <Link to={`/profile?userId=${follower.userId}`} className="follower-link">
                                <div className="follower-avatar">
                                    {follower.avatar ? (
                                        <img src={follower.avatar} alt={follower.username} />
                                    ) : (
                                        <FaUserCircle className="default-avatar" />
                                    )}
                                </div>
                                <div className="follower-info">
                                    <span className="follower-username">{follower.username}</span>
                                    {follower.fullName && (
                                        <span className="follower-name">{follower.fullName}</span>
                                    )}
                                </div>
                            </Link>
                        </li>
                    ))}
                </ul>
            ) : (
                <div className="empty-followers">
                    {searchTerm ? (
                        <>
                            <FaUserFriends className="empty-icon" />
                            <p>No followers match your search</p>
                        </>
                    ) : (
                        <>
                            <FaUserFriends className="empty-icon" />
                            <p>No followers yet</p>
                        </>
                    )}
                </div>
            )}
        </div>
    );
}

export default ViewFollowers;