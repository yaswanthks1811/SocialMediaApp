import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Loading from '../Shared/Loading';
import ErrorMessage from '../Shared/ErrorMessage';
import { registerUser } from '../services/authService';
import { FaUser, FaEnvelope, FaLock, FaUserPlus } from 'react-icons/fa';
import './Register.css';

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const result = await registerUser(username, password, email);
            
            if (result && result.message === "Registration successful.") {
                navigate('/login');
            } else if (result && !result.success) {
                setError(result.error || 'Registration failed. Please check your details.');
            }
        } catch (err) {
            console.error("Registration error:", err);
            setError(err.message || 'An unexpected error occurred during registration.');
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <Loading />;

    return (
        <div className="register-container">
            <div className="register-card">
                <div className="register-header">
                    <h2><FaUserPlus className="header-icon" /> Create Account</h2>
                    <p>Join now!</p>
                </div>

                {error && <ErrorMessage message={error} />}

                <form onSubmit={handleSubmit} className="register-form">
                    <div className="form-group">
                        <label htmlFor="username" className="form-label">
                            <FaUser className="input-icon" />
                            Username
                        </label>
                        <input
                            type="text"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="form-input"
                            placeholder="Choose a username"
                            required
                            autoComplete="username"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email" className="form-label">
                            <FaEnvelope className="input-icon" />
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="form-input"
                            placeholder="Enter your email"
                            required
                            autoComplete="email"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password" className="form-label">
                            <FaLock className="input-icon" />
                            Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="form-input"
                            placeholder="Create a password"
                            required
                            autoComplete="new-password"
                        />
                    </div>

                    <button 
                        type="submit" 
                        className="register-button"
                        disabled={loading}
                    >
                        {loading ? 'Creating Account...' : 'Register'}
                    </button>
                </form>

                <div className="register-footer">
                    <p>Already have an account? <a href="/login" className="footer-link">Sign in</a></p>
                </div>
            </div>
        </div>
    );
}

export default RegisterPage;