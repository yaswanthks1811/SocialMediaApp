import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  FaHome, 
  FaSearch, 
  FaPlusCircle, 
  FaPeopleArrows,
  FaBolt, 
  FaUser,
  FaBell,
  FaEnvelope,
  FaSignOutAlt
} from 'react-icons/fa';
import './NavBar.css'; // Make sure this CSS file exists and is correctly styled
import { logoutUser } from '../services/api'; // Ensure this service function exists and works

// Use object destructuring to get the 'type' prop
function Navbar({ type='1' }) { // <--- FIX 1: Destructure props
  const navigate = useNavigate();

  const handleLogout = async () => { // <--- FIX 3: Make async
    console.log('Logout clicked');
    try {
      await logoutUser(); // <--- FIX 3: Call the actual logout function
      console.log('Logout successful via service');
      navigate('/'); // Navigate after successful logout
    } catch (error) {
      console.error('Logout failed:', error);
      // Optionally display an error message to the user
    }
  };

  // Use strict equality (===) and check for the specific value you intend to pass
  if (type === '1') { // <--- FIX 4 & 5: Use strict equality and maybe a clearer value
    return (
      <nav className="bottom-navbar">
        {/* Links for logged-in user */}
        <Link to="/feed" className="nav-item">
          <FaHome className="nav-icon" />
          <span className="nav-text">Feed</span>
        </Link>
        <Link to="/inbox" className="nav-item">
          <FaEnvelope className="nav-icon" />
          <span className="nav-text">Inbox</span>
        </Link>
        <Link to="/search" className="nav-item">
          <FaSearch className="nav-icon" />
          <span className="nav-text">Search</span>
        </Link>
        <Link to="/uploadPost" className="nav-item highlight">
          <FaPlusCircle className="nav-icon highlight-icon" />
          <span className="nav-text">Create</span>
        </Link>
        <Link to="/notifications" className="nav-item">
          <FaBell className="nav-icon" />
          <span className="nav-text">Notifications</span>
        </Link>
        <Link to="/profile" className="nav-item">
          <FaUser className="nav-icon" />
          <span className="nav-text">Profile</span>
        </Link>
        <button onClick={handleLogout} className="nav-item logout-item">
          <FaSignOutAlt className="nav-icon" />
          <span className="nav-text">Logout</span>
        </button>
      </nav>
    );
  } else if (type === '2') { // Example for a guest type
    // Return statement for the 'else' condition
    return ( // <--- FIX 2: Add return statement
      <nav className="bottom-navbar guest-navbar"> {/* Optional: different class for styling */}
        {/* Links for guest user */}
        <Link to="/feed" className="nav-item">
          <FaHome className="nav-icon" />
          <span className="nav-text">Feed</span>
        </Link>
        <Link to="/trending" className="nav-item">
          <FaBolt className="nav-icon" />
          <span className="nav-text">Trending</span>
        </Link>
        <Link to="/uploadPost" className="nav-item highlight">
          <FaPlusCircle className="nav-icon highlight-icon" />
          <span className="nav-text">Create</span>
        </Link>
        <Link to="/recommendations" className="nav-item">
          <FaPeopleArrows className="nav-icon" />
          <span className="nav-text">Suggestions</span>
        </Link>
        <button onClick={handleLogout} className="nav-item logout-item">
          <FaSignOutAlt className="nav-icon" />
          <span className="nav-text">Logout</span>
        </button>
        
      </nav>
    );
  } else {
    // Optional: Return null or a default minimal navbar if type is not recognized
    return null; 
  }
}

export default Navbar;