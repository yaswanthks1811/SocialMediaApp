import React, { useState, useEffect } from 'react';
import { getInbox } from '../services/api';
import { Link } from 'react-router-dom';
import { FaEnvelope, FaUserCircle, FaSearch, FaChevronRight } from 'react-icons/fa';
import './MessageInbox.css';
import Navbar from '../components/NavBar';

function MessageInbox() {
  const [senders, setSenders] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    const fetchInbox = async () => {
      setLoading(true);
      try {
        const result = await getInbox();
        if (result.success) {
          setSenders(result.data);
          setError(null);
        } else {
          setError(result.error);
          setSenders([]);
        }
      } catch (err) {
        setError('Failed to fetch inbox');
        setSenders([]);
      }
      setLoading(false);
    };

    fetchInbox();
  }, []);

  const filteredSenders = senders.filter(sender =>
    sender.username.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return (
      <div className="inbox-container">
        <div className="loading-spinner"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="inbox-container">
        <div className="error-message">
          <FaEnvelope className="error-icon" />
          <p>{error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="inbox-container">
      <div className="inbox-header">
        <h2><FaEnvelope className="header-icon" /> Messages</h2>
        <div className="search-bar">
          <FaSearch className="search-icon" />
          <input
            type="text"
            placeholder="Search messages..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {filteredSenders.length > 0 ? (
        <ul className="sender-list">
          {filteredSenders.map((sender) => (
            <li key={sender.userId} className="sender-item">
              <Link to={`/viewMessages?senderId=${sender.userId}`} className="sender-link">
                <div className="sender-avatar">
                  {sender.avatar ? (
                    <img src={sender.avatar} alt={sender.username} />
                  ) : (
                    <FaUserCircle className="default-avatar" />
                  )}
                </div>
                <div className="sender-info">
                  <span className="sender-name">{sender.username}</span>
                  <span className="sender-preview">{sender.lastMessagePreview || " "}</span>
                </div>
                <FaChevronRight className="chevron-icon" />
              </Link>
            </li>
          ))}
        </ul>
      ) : (
        <div className="empty-inbox">
          <FaEnvelope className="empty-icon" />
          <p>Your inbox is empty</p>
          {searchTerm && <p>No matches for "{searchTerm}"</p>}
        </div>
      )}
      <Navbar className="bottom-navbar" />
      
    </div>
  );
}

export default MessageInbox;