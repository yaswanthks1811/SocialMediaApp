import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom'; // If you're using React Router for user profiles
import { followUser, unfollowUser } from '../services/api';

function FollowButton({ userId , isFollowing}) {
  const [isSelf, setIsSelf] = useState(false);
  const loggedInUserId = getLoggedInUserId(); // Replace with your actual logic
  

  useEffect(() => {
    if (userId && loggedInUserId) {
      setIsSelf(parseInt(userId) === parseInt(loggedInUserId));
    } else {
      setIsSelf(false); // Handle cases where IDs might not be immediately available
    }
  }, [userId, loggedInUserId]);

  const handleFollow = async () => {
    followUser(userId);
     
  };

  const handleUnfollow = async () => {
    // API call to unfollow the user
    unfollowUser(userId);
     
   
  };
  if(userId==null){
    return null;
  }

  if (isSelf) {
    return null; // Don't show the button if it's the logged-in user's profile
  }

  return (
    <button onClick={isFollowing ? handleUnfollow : handleFollow}>
      {isFollowing ? 'Unfollow' : 'Follow'}
    </button>
  );
}

// Helper function to get the logged-in user's ID (replace with your actual implementation)
function getLoggedInUserId() {
  // Example using localStorage (not recommended for sensitive data)
  return localStorage.getItem('userId');

  // Example using a global state management system (like Redux or Context API)
  // const authState = useSelector(state => state.auth);
  // return authState.userId;

  // Example using a React Context
  // const { userId } = useContext(AuthContext);
  // return userId;

  // Replace with your actual way of accessing the logged-in user's ID
}

export default FollowButton;