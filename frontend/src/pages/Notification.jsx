import React, { useState, useEffect } from 'react';
import { getNotifications, markNotificationAsRead } from '../services/api';
import { Link } from 'react-router-dom';
import Navbar from '../components/NavBar';
import { 
  FaHeart, 
  FaComment, 
  FaUserPlus, 
  FaBell, 
  FaCheck,
  FaSpinner,
} from 'react-icons/fa';
import './Notification.css';

function Notifications() {
  const [notifications, setNotifications] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [activeFilter, setActiveFilter] = useState('all');

  useEffect(() => {
    const fetchNotifications = async () => {
      setLoading(true);
      try {
        const result = await getNotifications();
        if (result.success) {
          setNotifications(result.data);
          setError(null);
        } else {
          setError(result.error);
          setNotifications([]);
        }
      } catch (err) {
        console.error('Error fetching notifications:', err);
        setError('Failed to load notifications');
        setNotifications([]);
      }
      setLoading(false);
    };

    fetchNotifications();
  }, []);

  const handleMarkAsRead = async (notificationId) => {
    try {
      await markNotificationAsRead(notificationId);
      setNotifications(prev => prev.map(n => 
        n.notificationId === notificationId ? { ...n, read: true } : n
      ));
    } catch (err) {
      console.error('Error marking as read:', err);
      setError('Failed to mark notification as read');
    }
  };

  const filteredNotifications = notifications.filter(notification => {
    if (activeFilter === 'all') return true;
    if (activeFilter === 'unread') return !notification.read;
    return notification.type === activeFilter;
  });

  const getNotificationIcon = (type) => {
    switch (type) {
      case 'like': return <FaHeart className="like-icon" />;
      case 'comment': return <FaComment className="comment-icon" />;
      case 'follow': return <FaUserPlus className="follow-icon" />;
      default: return <FaBell className="default-icon" />;
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
      <div className="notifications-container">
        <div className="error-message">
          <FaBell className="error-icon" />
          <p>{error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="notifications-container">
      <div className="notifications-header">
        <h2><FaBell className="header-icon" /> Notifications</h2>
        
        <div className="filter-tabs">
          <button 
            className={`filter-tab ${activeFilter === 'all' ? 'active' : ''}`}
            onClick={() => setActiveFilter('all')}
          >
            All
          </button>
          <button 
            className={`filter-tab ${activeFilter === 'unread' ? 'active' : ''}`}
            onClick={() => setActiveFilter('unread')}
          >
            Unread
          </button>
          <button 
            className={`filter-tab ${activeFilter === 'like' ? 'active' : ''}`}
            onClick={() => setActiveFilter('like')}
          >
            Likes
          </button>
          <button 
            className={`filter-tab ${activeFilter === 'comment' ? 'active' : ''}`}
            onClick={() => setActiveFilter('comment')}
          >
            Comments
          </button>
        </div>
      </div>

      {filteredNotifications.length > 0 ? (
        <ul className="notifications-list">
          {filteredNotifications.map((notification) => (
            <li 
              key={notification.notificationId} 
              className={`notification-item ${!notification.read ? 'unread' : ''}`}
            >
              <div className="notification-icon">
                {getNotificationIcon(notification.type)}
              </div>
              
              <div className="notification-content">
                {notification.type === 'like' && (
                  <p>
                    <Link to={`/profile?userId=${notification.senderUserId}`} className="sender-link">
                      {notification.senderUsername}
                    </Link>{' '}
                    liked your{' '}
                    <Link to={`/admin/post/${notification.postId}`} className="post-link">
                      post
                    </Link>
                  </p>
                )}
                
                {notification.type === 'comment' && (
                  <p>
                    <Link to={`/profile?userId=${notification.senderUserId}`} className="sender-link">
                      {notification.senderUsername}
                    </Link>{' '}
                    commented on your{' '}
                    <Link to={`/admin/post/${notification.postId}`} className="post-link">
                      post
                    </Link>
                  </p>
                )}
                
                {notification.type === 'follow' && (
                  <p>
                    <Link to={`/profile?userId=${notification.senderUserId}`} className="sender-link">
                      {notification.senderUsername}
                    </Link>{' '}
                    started following you
                  </p>
                )}
                
                {/* <div className="notification-time">
                  {new Date(notification.createdAt).toLocaleString()}
                </div> */}
              </div>
              
              {!notification.read && (
                <button 
                  className="mark-read-btn"
                  onClick={() => handleMarkAsRead(notification.notificationId)}
                  title="Mark as read"
                >
                  <FaCheck />
                </button>
              )}
            </li>
          ))}
        </ul>
      ) : (
        <div className="empty-notifications">
          <FaBell className="empty-icon" />
          <p>No {activeFilter === 'all' ? '' : activeFilter} notifications</p>
        </div>
      )}
      <Navbar className="bottom-navbar" />
    </div>
  );
}

export default Notifications;