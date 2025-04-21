import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { viewFollowing } from '../services/api';
import { FaUserCheck, FaUserCircle, FaSpinner, FaSearch } from 'react-icons/fa';
import './ViewFollowing.css'; // Can share CSS with FollowList with minor adjustments

function ViewFollowing() {
    const { userId } = useParams();
    const [following, setFollowing] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const fetchFollowing = async () => {
            setLoading(true);
            try {
                const response = await viewFollowing(userId);
                if (response.success) {
                    setFollowing(response.data.users);
                    setError(null);
                } else {
                    setError(response.error || 'Failed to load following');
                }
            } catch (error) {
                console.error('Error fetching following:', error);
                setError('Failed to load following.');
            }
            setLoading(false);
        };

        fetchFollowing();
    }, [userId]);

    const filteredFollowing = following.filter(user =>
        user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
        (user.fullName && user.fullName.toLowerCase().includes(searchTerm.toLowerCase()))
    );

    if (loading) {
        return (
            <div className="following-container">
                <div className="loading-spinner">
                    <FaSpinner className="spinner-icon" />
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="following-container">
                <div className="error-message">
                    <FaUserCheck className="error-icon" />
                    <p>{error}</p>
                </div>
            </div>
        );
    }

    return (
        <div className="following-container">
            <div className="following-header">
                <h2><FaUserCheck className="header-icon" /> Following</h2>
                
                <div className="search-bar">
                    <FaSearch className="search-icon" />
                    <input
                        type="text"
                        placeholder="Search following..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
            </div>

            {filteredFollowing.length > 0 ? (
                <ul className="following-list">
                    {filteredFollowing.map(user => (
                        <li key={user.userId} className="following-item">
                            <Link to={`/profile?userId=${user.userId}`} className="following-link">
                                <div className="following-avatar">
                                    {user.avatar ? (
                                        <img src={user.avatar} alt={user.username} />
                                    ) : (
                                        <FaUserCircle className="default-avatar" />
                                    )}
                                </div>
                                <div className="following-info">
                                    <span className="following-username">{user.username}</span>
                                    {user.fullName && (
                                        <span className="following-name">{user.fullName}</span>
                                    )}
                                    {user.isFollowingBack && (
                                        <span className="following-badge">Follows you</span>
                                    )}
                                </div>
                            </Link>
                        </li>
                    ))}
                </ul>
            ) : (
                <div className="empty-following">
                    {searchTerm ? (
                        <>
                            <FaUserCheck className="empty-icon" />
                            <p>No matching users found</p>
                        </>
                    ) : (
                        <>
                            <FaUserCheck className="empty-icon" />
                            <p>Not following anyone yet</p>
                        </>
                    )}
                </div>
            )}
        </div>
    );
}

export default ViewFollowing;