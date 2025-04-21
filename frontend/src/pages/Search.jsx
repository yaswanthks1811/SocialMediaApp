import React, { useState, useEffect } from 'react';
import { searchUsers } from '../services/api';
import { Link } from 'react-router-dom';
import { FaSearch, FaUserCircle, FaSpinner } from 'react-icons/fa';
import './Search.css';
import Navbar from '../components/NavBar';

function Search() {
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);

  // Add debounce to prevent excessive API calls
  useEffect(() => {
    const timer = setTimeout(() => {
      if (searchTerm.trim()) {
        performSearch();
      } else {
        setSearchResults([]);
        setError(null);
      }
    }, 500);

    return () => clearTimeout(timer);
  }, [searchTerm]);

  const performSearch = async () => {
    setLoading(true);
    setHasSearched(true);
    try {
      const result = await searchUsers(searchTerm);
      if (result.success) {
        setSearchResults(result.data);
        setError(null);
      } else {
        setError(result.error || 'No results found');
        setSearchResults([]);
      }
    } catch (err) {
      console.error('Search error:', err);
      setError('Failed to perform search');
      setSearchResults([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="search-container">
      <div className="search-header">
        <h2>Search</h2>
        <div className="search-bar">
          <FaSearch className="search-icon" />
          <input
            type="text"
            placeholder="Search for users..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            autoFocus
          />
          {loading && <FaSpinner className="spinner-icon" />}
        </div>
      </div>

      <div className="search-results">
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        {hasSearched && !loading && searchResults.length === 0 && !error && (
          <div className="empty-results">
            <FaUserCircle className="empty-icon" />
            <p>No users found</p>
          </div>
        )}

        {searchResults.length > 0 && (
          <ul className="results-list">
            {searchResults.map((user) => (
              <li key={user.userId} className="result-item">
                <Link to={`/profile?userId=${user.userId}`} className="user-link">
                  <div className="user-avatar">
                    {user.avatar ? (
                      <img src={user.avatar} alt={user.username} />
                    ) : (
                      <FaUserCircle className="default-avatar" />
                    )}
                  </div>
                  <div className="user-info">
                    <span className="username">{user.username}</span>
                    {user.fullName && <span className="fullname">{user.fullName}</span>}
                  </div>
                </Link>
              </li>
            ))}
          </ul>
        )}
      </div>
      <Navbar className="bottom-navbar" type='2' />
    </div>
  );
}

export default Search;