import React, { useState, useEffect } from 'react';
import { useParams, useSearchParams, Link } from 'react-router-dom';
import { getProfile, toggleFollow, deletePost, followUser } from '../services/api';
import Post from '../components/Post';
import Navbar from '../components/NavBar';
import './Profile.css';
import he from 'he';
import { 
  FaSpinner, 
  FaTrash, 
  FaUserEdit, 
  FaEnvelope, 
  FaUserFriends,
  FaUserCheck,
  FaUserPlus,
  FaImages
} from 'react-icons/fa';
import { MdPostAdd } from 'react-icons/md';

function Profile() {
    const [searchParams] = useSearchParams();
    const userId = searchParams.get('userId');
    const [profile, setProfile] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isFollowing, setIsFollowing] = useState(false);

    useEffect(() => {
        const fetchProfile = async () => {
            setLoading(true);
            try {
                const result = await getProfile(userId);
                if (result.success) {
                    setProfile(result.data);
                    setError(null);
                    setIsFollowing(result.data.isFollowing);
                } else {
                    setError(result.error);
                    setProfile(null);
                }
            } catch (err) {
                setError('Failed to load profile');
                console.error('Profile load error:', err);
            }
            setLoading(false);
        };

        fetchProfile();
    }, [userId]);

    const handleFollowToggle = async () => {
        try {
            const action = isFollowing ? 'unfollow' : 'follow';
            
            const result = await followUser(userId, action);
            if (result.success) {
                setIsFollowing(!isFollowing);
                setProfile(prev => ({
                    ...prev,
                    followers: isFollowing ? prev.followers - 1 : prev.followers + 1
                }));
            } else {
                setError(result.error);
            }
        } catch (err) {
            console.error('Follow toggle error:', err);
            setError('Failed to toggle follow.');
        }
    };

    const handleDeletePost = async (postId) => {
        if (window.confirm('Are you sure you want to delete this post?')) {
            try {
                const result = await deletePost(postId);
                if (result.success) {
                    setProfile(prev => ({
                        ...prev,
                        posts: prev.posts.filter(post => post.postId !== postId)
                    }));
                } else {
                    setError(result.error);
                }
            } catch (err) {
                console.error('Delete post error:', err);
                setError('Failed to delete post.');
            }
        }
    };

    if (loading) {
        return (
            <div className="profile-loading">
                <FaSpinner className="spinner" />
                <p>Loading profile...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="profile-error">
                <p className="error-text">{error}</p>
                <button onClick={() => window.location.reload()} className="retry-button">
                    Try Again
                </button>
            </div>
        );
    }

    if (!profile) {
        return (
            <div className="profile-not-found">
                <p>Profile not found</p>
                <Link to="/" className="home-link">Go to Home</Link>
            </div>
        );
    }

    return (
        <div className="profile-container">
            {/* Profile Header Section */}
            <div className="profile-header">
                <div className="profile-avatar">
                    {profile.user.avatar ? (
                        <img src={profile.user.avatar} alt={profile.user.username} />
                    ) : (
                        <div className="avatar-placeholder">
                            {profile.user.username.charAt(0).toUpperCase()}
                        </div>
                    )}
                </div>
                <div className="profile-info">
                    <h1 className="profile-username">{profile.user.username}</h1>
                    <p className="profile-name">
                        {profile.user.firstName} {profile.user.lastName}
                    </p>
                </div>
            </div>

            {/* Bio Section */}
            <div className="profile-bio-section">
                <h3 className="section-title">
                    <FaUserEdit className="section-icon" /> Bio
                </h3>
                <div className="profile-bio-content">
                    {profile.user.bio ? (
                        <p>{he.decode(profile.user.bio)}</p>
                    ) : (
                        <p className="empty-bio">No bio yet</p>
                    )}
                </div>
            </div>

            {/* Stats Section */}
            <div className="profile-stats-section">
                <Link to={`/viewFollowers?userId=${profile.user.userId}`} className="stat-item">
                    <span className="stat-number">{profile.followers}</span>
                    <span className="stat-label">Followers</span>
                </Link>
                <Link to={`/viewFollowing?userId=${profile.user.userId}`} className="stat-item">
                    <span className="stat-number">{profile.following}</span>
                    <span className="stat-label">Following</span>
                </Link>
                <div className="stat-item">
                    <span className="stat-number">{profile.posts?.length || 0}</span>
                    <span className="stat-label">Posts</span>
                </div>
            </div>

            {/* Action Buttons */}
            <div className="profile-actions">
                {profile.isOwner ? (
                    <>
                        <Link to="/editProfile" className="action-button edit-button">
                            <FaUserEdit /> Edit Profile
                        </Link>
                        <Link to="/uploadPost" className="action-button create-button">
                            <MdPostAdd /> Create Post
                        </Link>
                    </>
                ) : (
                    <>
                        <button 
                            onClick={handleFollowToggle}
                            className={`action-button ${isFollowing ? 'unfollow-button' : 'follow-button'}`}
                        >
                            {isFollowing ? (
                                <>
                                    <FaUserCheck /> Following
                                </>
                            ) : (
                                <>
                                    <FaUserPlus /> Follow
                                </>
                            )}
                        </button>
                        <Link 
                            to={`/viewMessages?senderId=${userId}`}
                            className="action-button message-button"
                        >
                            <FaEnvelope /> Message
                        </Link>
                    </>
                )}
            </div>

            {/* Posts Section */}
            <div className="profile-posts-section">
                <h3 className="section-title">
                    <FaImages className="section-icon" /> Posts
                </h3>
                {profile.posts?.length > 0 ? (
                    <div className="posts-grid">
                        {profile.posts.map((post) => (
                            <div key={post.postId} className="post-item">
                                <Post post={post} />
                                {profile.isOwner && (
                                    <button 
                                        onClick={() => handleDeletePost(post.postId)}
                                        className="delete-post-button"
                                        title="Delete post"
                                    >
                                        <FaTrash />
                                    </button>
                                )}
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="no-posts">
                        <p>No posts yet</p>
                        {profile.isOwner && (
                            <Link to="/uploadPost" className="create-post-link">
                                Create your first post
                            </Link>
                        )}
                    </div>
                )}
            </div>
            <br></br>
            <br></br>

            <Navbar className="bottom-navbar" />
        </div>
    );
}

export default Profile;