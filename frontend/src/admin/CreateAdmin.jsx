import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './CreateAdmin.css';
const API_BASE_URL = 'http://localhost:8080/SocialMediaApp';

function CreateAdmin() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`${API_BASE_URL}/api/admin/createAdmin`, {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `username=${username}&password=${password}`,
            });
            const data = await response.json();
            setMessage(data.message);
            if (data.success) {
                setUsername('');
                setPassword('');
            }
        } catch (err) {
            console.error('Error creating admin:', err);
            setMessage('Failed to create admin.');
        }
    };

    return (
        <div className="create-admin-container">
            <h2>Create New Admin</h2>
            {message && <p className={message.includes('success') ? 'success-message' : 'error-message'}>{message}</p>}
            <form onSubmit={handleSubmit} className="create-admin-form">
                <div className="form-group">
                    <label htmlFor="username">Username:</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        className="form-control"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="form-control"
                        required
                    />
                </div>
                <button type="submit" className="create-button">Create Admin</button>
            </form>
            <Link to="/admin" className="back-button">Back to Dashboard</Link>
        </div>
    );
}

export default CreateAdmin;