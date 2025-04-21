// src/services/api.js
import axios from 'axios';

const BASE_URL = 'http://localhost:8080/SocialMediaApp/api';

const api = axios.create({
    baseURL: BASE_URL,
    withCredentials: true,
});

export const fetchFeed = async () => {
    try {
        const response = await api.get('/feed');
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, data: null, error: "User not logged in." };
        }
        console.error('Error fetching feed:', error);
        return { success: false, data: null, error: error.message || 'Could not retrieve feed.' };
    }
};

export const likePost = async (id) => {
    try {
        console.log(id);
        await api.post('/likePost', null, { // Use 'null' as the data body if only sending query params
            params: {
              postId: id,
            },
          });        return { success: true, message: 'Post liked/unliked successfully.', error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        } else if (error.response && error.response.status === 400) {
            return { success: false, error: 'Missing post ID.' };
        }
        console.error('Error liking/unliking post:', error);
        return { success: false, error: error.message || 'Could not like/unlike post.' };
    }
};


export const addComment = async (postId, commentText) => {
    try {
        const response =await api.post('/addComment', null, { 
            params: {
              postId: postId,
              commentText :commentText,
            },
          }); 
        // const response = await api.post(`/addComment`, { postId, commentText });
        return { success: true, data: response.data, error: null }; // Return the new comment data
    } catch (error) {
        console.error('Error adding comment:', error);
        return { success: false, data: null, error: error.message || 'Failed to add comment' };
    }
};

export const getProfile = async (userId = null) => {
    try {
        console.log(userId);
        const params = userId ? `?id=${userId}` : '';
        const response = await api.get(`/profile${params}`);
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        }
        console.error('Error fetching profile:', error);
        return { success: false, error: error.message || 'Could not retrieve profile.' };
    }
};
export const updateProfile = async (profileData) => {
    console.log(profileData);
    try {
        const response = await api.post('/editProfile', 
            JSON.stringify({
            firstname: profileData.firstname,
            lastname: profileData.lastname,
            bio: profileData.bio,
            email: profileData.email,
            password: profileData.password,
        }) // Will be undefined if not provided
        , {
            headers: {
                'Content-Type': 'application/json', // Ensure you're sending JSON
            },
        });
        return { success: true, data: response.data, error: null };
    } catch (error) {
        console.error('Error updating profile:', error);
        return { success: false, data: null, error: error.message || 'Failed to update profile' };
    }
};
// Profile APIs
export const followUser = async (targetUserId) => {
    try {
        await api.post('/follow', { targetUserId });
        return { success: true, message: 'User followed successfully.', error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        } else if (error.response && error.response.status === 400) {
            return { success: false, error: 'Missing target user ID.' };
        }
        console.error('Error following user:', error);
        return { success: false, error: error.message || 'Could not follow user.' };
    }
};

export const unfollowUser = async (targetUserId) => {
    try {
        await api.post('/unfollow', { targetUserId });
        return { success: true, message: 'User unfollowed successfully.', error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        } else if (error.response && error.response.status === 400) {
            return { success: false, error: 'Missing targetUserId.' };
        }
        console.error('Error unfollowing user:', error);
        return { success: false, error: error.message || 'Could not unfollow user.' };
    }
};

export const deletePost = async (postId) => {
    try {
        const response = await api.post('/deletePost', { 
            targetPostId: postId 
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.data && response.data.error) {
            return { success: false, error: response.data.error };
        }

        return { success: true, error: null };
    } catch (error) {
        console.error('Error deleting post:', error);
        
        // Handle different error scenarios
        let errorMessage = 'Failed to delete post';
        if (error.response) {
            // The request was made and the server responded with a status code
            if (error.response.data && error.response.data.error) {
                errorMessage = error.response.data.error;
            } else {
                errorMessage = `Server error: ${error.response.status}`;
            }
        } else if (error.request) {
            // The request was made but no response was received
            errorMessage = 'No response from server';
        }

        return { success: false, error: errorMessage };
    }
};
// Messages
export const sendMessage = async (receiverId, message) => {
    console.log(message);
    try {
        const response = await api.post(
                  `/sendMessage`,
                  {
                    receiverId: receiverId,
                    message: message,
                  });
                  return { success: true, message: 'Message sent successfully.', error: null };
                
       
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        } else if (error.response && error.response.status === 400) {
            return { success: false, error: 'Missing parameters.' };
        }
        console.error('Error sending message:', error);
        return { success: false, error: error.message || 'Failed to send message.' };
    }
};
// Messages
export const getInbox = async () => {
    try {
        const response = await api.get('/inbox');
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        }
        console.error('Error fetching inbox:', error);
        return { success: false, error: error.message || 'Unable to fetch Messages.' };
    }
};

export const getNotifications = async () => {
    try {
        const response = await api.get('/notifications');
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        }
        console.error('Error fetching notifications:', error);
        return { success: false, error: error.message || 'Failed to fetch notifications.' };
    }
};

export const markNotificationAsRead = async (notificationId) => {
    try {
        await api.post('/notifications', { notificationId });
        return { success: true, error: null };
    } catch (error) {
        console.error('Error marking notification as read:', error);
        return { success: false, error: error.message || 'Failed to mark notification as read.' };
    }
};

// Post APIs
export const uploadPost = async (postData) => {
    try {
        const formData = new FormData();
        formData.append('caption', postData.caption);
        formData.append('postType', postData.postType);

        if (postData.postType.toLowerCase() === 'image' && postData.image) {
            formData.append('image', postData.image);
        } else if (postData.postType.toLowerCase() === 'video' && postData.video) {
            formData.append('video', postData.video);
        }

        const response = await api.post('/uploadPost', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        }
        console.error('Error uploading post:', error);
        return { success: false, error: error.message || 'Cannot Upload Post.' };
    }
};

// Recommendations
export const getRecommendations = async () => {
    try {
        const response = await api.get('/recommendations');
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        }
        console.error('Error fetching recommendations:', error);
        return { success: false, error: error.message || 'Unable to fetch recommendations.' };
    }
};

export const searchUsers = async (query) => {
    try {
        const response = await api.get(`/search?query=${query}`);
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        }
        console.error('Error searching users:', error);
        return { success: false, error: error.message || 'Failed to search users.' };
    }
};


// Follower
export const toggleFollow = async (targetUserId, action) => {
    try {
        await api.post('/toggleFollow', { targetUserId, action });
        const message = action === 'follow' ? 'User followed successfully.' : 'User unfollowed successfully.';
        return { success: true, message, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        } else if (error.response && error.response.status === 400) {
            return { success: false, error: 'Missing parameters.' };
        }
        console.error('Error toggling follow:', error);
        return { success: false, error: error.message || 'Could not toggle follow.' };
    }
};

export const getTrendingPosts = async () => {
    try {
        const response = await api.get('/trending');
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        }
        console.error('Error fetching trending posts:', error);
        return { success: false, error: error.message || 'Failed to retrieve trending posts.' };
    }
};


export const viewFollowers = async (profileUserId = null) => {
    try {
        const params = profileUserId ? `?id=${profileUserId}` : '';
        const response = await api.get(`/viewFollowers${params}`);
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        }
        console.error('Error fetching followers:', error);
        return { success: false, error: error.message || 'Failed to retrieve followers.' };
    }
};

export const viewFollowing = async (profileUserId = null) => {
    try {
        const params = profileUserId ? `?id=${profileUserId}` : '';
        const response = await api.get(`/viewFollowing${params}`);
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        }
        console.error('Error fetching following:', error);
        return { success: false, error: error.message || 'Failed to retrieve following users.' };
    }
}

export const viewMessages = async (senderId) => {
    try {
        const response = await api.get(`/viewMessages?senderId=${senderId}`);
        return { success: true, data: response.data, error: null };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return { success: false, error: 'User not logged in.' };
        } else if (error.response && error.response.status === 400) {
            return { success: false, error: 'Missing senderId.' };
        }
        console.error('Error fetching messages:', error);
        return { success: false, error: error.message || 'Failed to retrieve messages.' };
    }
};

export const logoutUser = async () => {
    try {
        
        await api.get('/logout'); // Example: POST request to /api/logout
        console.log('Backend logout call successful (or skipped).');
        localStorage.removeItem('userId');
        console.log('Local storage cleared for logout.');

        return { success: true, message: 'Logout successful.', error: null };

    } catch (error) {
        console.error('Error during logout:', error);

        if (error.response) {
            // e.g., maybe log specific server errors
             console.error('Logout API responded with status:', error.response.status);
        }
        // Return failure status
        return { success: false, error: error.message || 'Logout failed on server, cleared locally.' };
    }
};


export default api;