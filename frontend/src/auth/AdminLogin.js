import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginAdmin } from '../services/authService'; // Adjust the path if needed
import './AdminLogin.css';
import { FaUserLock } from 'react-icons/fa';
import { MdPerson, MdLock } from 'react-icons/md';

function AdminLogin() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setLoading(true);

        try {
            const data = await loginAdmin(username, password);
            localStorage.setItem('loggedInAdmin', JSON.stringify({
                adminId: data.adminId,
                adminUsername: data.adminUsername,
            }));
            setMessage('Admin login successful!');
            setTimeout(() => {
                navigate('/admin'); // Redirect after a short delay for message visibility
            }, 1000);

        } catch (error) {
            setMessage(error.message || 'Login failed. Please check your credentials.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="admin-login-container">
            <div className="admin-login-card">
                <div className="admin-login-header">
                    <h2>
                        <FaUserLock className="admin-icon" /> Admin Login
                    </h2>
                    <p>Enter your credentials to access the admin panel.</p>
                </div>
                <form onSubmit={handleSubmit} className="admin-login-form">
                    <div className="form-group">
                        <label htmlFor="username">
                            <MdPerson className="input-icon" style={{ marginRight: '8px' }} /> Username:
                        </label>
                        <div className="input-wrapper">
                            <MdPerson className="input-icon" />
                            <input
                                type="text"
                                id="username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="form-control"
                                required
                            />
                        </div>
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">
                            <MdLock className="input-icon" style={{ marginRight: '8px' }} /> Password:
                        </label>
                        <div className="input-wrapper">
                            <MdLock className="input-icon" />
                            <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="form-control"
                                required
                            />
                        </div>
                    </div>
                    <button type="submit" className="login-button" disabled={loading}>
                        {loading ? 'Logging In...' : 'Login'}
                    </button>
                    {message && (
                        <div className="message-container">
                            <p className={message.includes('successful') ? 'success-message' : 'error-message'}>
                                {message}
                            </p>
                        </div>
                    )}
                </form>
            </div>
        </div>
    );
}

export default AdminLogin;