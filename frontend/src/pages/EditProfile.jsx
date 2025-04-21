import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProfile, updateProfile } from '../services/api';
import { FaUser ,FaSpinner, FaEnvelope, FaLock, FaPenAlt, FaTimes, FaCheck } from 'react-icons/fa';
import './EditProfile.css';

function EditProfile() {
    const navigate = useNavigate();
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [formData, setFormData] = useState({
        firstname: '',
        lastname: '',
        bio: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [passwordMatchError, setPasswordMatchError] = useState('');

    useEffect(() => {
        const fetchProfile = async () => {
            setLoading(true);
            try {
                const result = await getProfile();
                if (result.success) {
                    setProfile(result.data);
                    setFormData({
                        firstname: result.data.user.firstName || '',
                        lastname: result.data.user.lastName || '',
                        bio: result.data.user.bio || '',
                        email: result.data.user.email || '',
                        password: '',
                        confirmPassword: ''
                    });
                    setError(null);
                } else {
                    setError(result.error);
                }
            } catch (error) {
                console.error('Error fetching profile:', error);
                setError('Failed to load profile information.');
            }
            setLoading(false);
        };

        fetchProfile();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        if (name === 'password' || name === 'confirmPassword') {
            if (formData.password && formData.confirmPassword && 
                (name === 'password' ? value !== formData.confirmPassword : value !== formData.password)) {
                setPasswordMatchError('Passwords do not match.');
            } else {
                setPasswordMatchError('');
            }
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (passwordMatchError) return;

        setLoading(true);
        try {
            const updatedData = {
                firstname: formData.firstname,
                lastname: formData.lastname,
                bio: formData.bio,
                email: formData.email,
                password: formData.password || undefined
            };
            
            const result = await updateProfile(updatedData);
            if (result.success) {
                navigate('/profile');
            } else {
                setError(result.error);
            }
        } catch (error) {
            console.error('Error updating profile:', error);
            setError('Failed to update profile.');
        }
        setLoading(false);
    };

    const handleCancel = () => {
        navigate('/profile');
    };

    if (loading) {
        return (
              <div className="loading-spinner">
                        <FaSpinner className="spinner" />
               </div>
            );
    }

    if (error) {
        return (
            <div className="error-container">
                <p className="error-text">Error: {error}</p>
                <button onClick={() => navigate('/profile')} className="back-button">
                    Back to Profile
                </button>
            </div>
        );
    }

    if (!profile) {
        return (
            <div className="error-container">
                <p>Could not load profile edit form.</p>
                <button onClick={() => navigate('/profile')} className="back-button">
                    Back to Profile
                </button>
            </div>
        );
    }

    return (
        <div className="edit-profile-container">
            <div className="edit-profile-header">
                <h2><FaPenAlt className="header-icon" /> Edit Profile</h2>
                <p>Update your personal information</p>
            </div>

            <form onSubmit={handleSubmit} className="edit-profile-form">
                <div className="form-grid">
                    <div className="form-group">
                        <label htmlFor="firstname" className="form-label">
                            <FaUser className="input-icon" />
                            First Name
                        </label>
                        <input
                            type="text"
                            id="firstname"
                            name="firstname"
                            value={formData.firstname}
                            onChange={handleChange}
                            className="form-input"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="lastname" className="form-label">
                            <FaUser className="input-icon" />
                            Last Name
                        </label>
                        <input
                            type="text"
                            id="lastname"
                            name="lastname"
                            value={formData.lastname}
                            onChange={handleChange}
                            className="form-input"
                        />
                    </div>

                    <div className="form-group full-width">
                        <label htmlFor="bio" className="form-label">
                            Bio
                        </label>
                        <textarea
                            id="bio"
                            name="bio"
                            value={formData.bio}
                            onChange={handleChange}
                            className="form-textarea"
                            rows="4"
                            placeholder="Tell us about yourself..."
                        />
                    </div>

                    <div className="form-group full-width">
                        <label htmlFor="email" className="form-label">
                            <FaEnvelope className="input-icon" />
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            className="form-input"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password" className="form-label">
                            <FaLock className="input-icon" />
                            New Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            className="form-input"
                            placeholder="Leave blank to keep current"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirmPassword" className="form-label">
                            <FaLock className="input-icon" />
                            Confirm Password
                        </label>
                        <input
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            className="form-input"
                        />
                        {passwordMatchError && (
                            <p className="error-message">{passwordMatchError}</p>
                        )}
                    </div>
                </div>

                <div className="form-actions">
                    <button
                        type="submit"
                        className="save-button"
                        disabled={loading || passwordMatchError}
                    >
                        <FaCheck className="button-icon" />
                        {loading ? 'Saving...' : 'Save Changes'}
                    </button>
                    <button
                        type="button"
                        className="cancel-button"
                        onClick={handleCancel}
                        disabled={loading}
                    >
                        <FaTimes className="button-icon" />
                        Cancel
                    </button>
                </div>

                {error && <p className="form-error-message">{error}</p>}
            </form>
        </div>
    );
}

export default EditProfile;