import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Post from '../components/Post';
import './ManagePosts.css'; // Import CSS for styling
import he from 'he';
const API_BASE_URL = 'http://localhost:8080/SocialMediaApp';


function ManagePosts() {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchPosts = async () => {
        setLoading(true);
        try {
            const response = await fetch(`${API_BASE_URL}/api/admin/posts`,{
                credentials: 'include',
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            setPosts(data.posts);
            setError(null);
        } catch (err) {
            console.error('Error fetching posts:', err);
            setError('Failed to load posts.');
        }
        setLoading(false);
    };

    useEffect(() => {
        fetchPosts();
    }, []);

    const handleDeletePost = async (postId) => {
        if (window.confirm('Are you sure you want to delete this post?')) {
            try {
                const response = await fetch(`${API_BASE_URL}/api/admin/deletePost`, {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `postId=${postId}`,
                    credentials: 'include',
                });
                if (response.ok) {
                    // Refetch posts after deletion
                    fetchPosts();
                } else {
                    const errorData = await response.json();
                    alert(`Failed to delete post: ${errorData?.message || 'Unknown error'}`);
                }
            } catch (err) {
                console.error('Error deleting post:', err);
                alert('Failed to delete post.');
            }
        }
    };

    if (loading) {
        return <p>Loading posts...</p>;
    }

    if (error) {
        return <p>Error: {error}</p>;
    }

    return (
        <div className="manage-posts-container">
            <h2>Manage Posts</h2>

            <Link to="/admin" className="back-button">
                Back to Dashboard
            </Link>
            {posts.length > 0 ? (
                <table className="posts-table">
                    <thead>
                        <tr>
                            <th>Post ID</th>
                            <th>Content</th>
                            <th>User ID</th>
                            <th>Created At</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {posts.map((post) => (
                            <tr key={post.postId}>
                                <td>{post.postId}</td>
                                 <td>
                                    <Link to={`/admin/post/${post.postId}`}>
                                        {he.decode(post.postCaption)?.substring(0, 50)}...
                                    </Link>
                                </td>
                                <td>{post.userId}</td>
                                <td>{new Date(post.createdAt).toLocaleString()}</td>
                                <td>
                                    <button
                                        onClick={() => handleDeletePost(post.postId)}
                                        className="delete-button"
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                <p>No posts found.</p>
            )}
            
        </div>
    );
}

export default ManagePosts;
