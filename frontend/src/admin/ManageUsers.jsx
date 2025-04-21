import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './ManageUsers.css';
const API_BASE_URL = 'http://localhost:8080/SocialMediaApp';


function ManageUsers() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchUsers = async () => {
        setLoading(true);
        try {
            const response = await fetch(`${API_BASE_URL}/api/admin/users`, {
                credentials: 'include', // Include credentials (cookies)
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            setUsers(data.users);
            setError(null);
        } catch (err) {
            console.error('Error fetching users:', err);
            setError('Failed to load users.');
        }
        setLoading(false);
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleDeleteUser = async (userId) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            try {
                const response = await fetch(`${API_BASE_URL}/api/admin/deleteUser`, {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `userId=${userId}`,
                });
                if (response.ok) {
                    fetchUsers();
                } else {
                    const errorData = await response.json();
                    alert(`Failed to delete user: ${errorData?.message || 'Unknown error'}`);
                }
            } catch (err) {
                console.error('Error deleting user:', err);
                alert('Failed to delete user.');
            }
        }
    };

    const handleBanUser = async (userId) => {
        try {
            const response = await fetch(`${API_BASE_URL}/api/admin/banUser`, {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `userId=${userId}`,
            });
            if (response.ok) {
                fetchUsers();
            } else {
                const errorData = await response.json();
                alert(`Failed to ban user: ${errorData?.message || 'Unknown error'}`);
            }
        } catch (err) {
            console.error('Error banning user:', err);
            alert('Failed to ban user.');
        }
    };

    const handleUnbanUser = async (userId) => {
        try {
            const response = await fetch(`${API_BASE_URL}/api/admin/unbanUser`, {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `userId=${userId}`,
            });
            if (response.ok) {
                fetchUsers();
            } else {
                const errorData = await response.json();
                alert(`Failed to unban user: ${errorData?.message || 'Unknown error'}`);
            }
        } catch (err) {
            console.error('Error unbanning user:', err);
            alert('Failed to unban user.');
        }
    };

    if (loading) {
        return <p>Loading users...</p>;
    }

    if (error) {
        return <p>Error: {error}</p>;
    }

    return (
        <div className="manage-users-container">
            <h2>Manage Users</h2>
            <Link to="/admin" className="back-button">
                Back to Dashboard
            </Link>
            <br />
            {users.length > 0 ? (
                <table className="users-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((user) => (
                            <tr key={user.userId}>
                                <td>{user.userId}</td>
                                <td>{user.username}</td>
                                <td>{user.email}</td>
                                <td>{user.accountStatus}</td>
                                <td>
                                    <button onClick={() => handleDeleteUser(user.userId)} className="delete-button">
                                        Delete
                                    </button>
                                    {user.accountStatus === 'ACTIVE' ? (
                                        <button onClick={() => handleBanUser(user.userId)} className="ban-button">
                                            Ban
                                        </button>
                                    ) : (
                                        <button onClick={() => handleUnbanUser(user.userId)} className="unban-button">
                                            Unban
                                        </button>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                <p>No users found.</p>
            )}
            
        </div>
    );
}

export default ManageUsers;
