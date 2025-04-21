import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FaUsers, FaNewspaper, FaUserShield, FaChartLine, FaSignOutAlt } from 'react-icons/fa';
import './AdminDashboard.css';
import { logoutAdmin } from '../services/authService';

function AdminDashboard() {
    const navigate = useNavigate();

    const handleLogout = async () => {
         console.log('Logout clicked');
            try {
              await logoutAdmin(); // <--- FIX 3: Call the actual logout function
              console.log('Logout successful via service');
              navigate('/admin/login'); // Navigate after successful logout
            } catch (error) {
              console.error('Logout failed:', error);
              // Optionally display an error message to the user
            }
    };

    return (
        <div className="admin-dashboard-container">
            <div className="admin-header">
                <div className="header-content">
                    <h2 className="admin-title">
                        <FaChartLine className="dashboard-icon" /> Admin Dashboard
                    </h2>
                    <p className="admin-subtitle">Manage your platform's content and users</p>
                </div>
                <button onClick={handleLogout} className="logout-button">
                    <FaSignOutAlt /> Logout
                </button>
            </div>
            
            <div className="admin-actions-grid">
                <Link to="/admin/users" className="admin-card">
                    <div className="card-content">
                        <div className="card-icon">
                            <FaUsers />
                        </div>
                        <h3>Manage Users</h3>
                        <p>View, edit, and manage all user accounts</p>
                    </div>
                </Link>
                
                <Link to="/admin/posts" className="admin-card">
                    <div className="card-content">
                        <div className="card-icon">
                            <FaNewspaper />
                        </div>
                        <h3>Manage Posts</h3>
                        <p>Review and moderate all user posts</p>
                    </div>
                </Link>
                
                <Link to="/admin/createAdmin" className="admin-card">
                    <div className="card-content">
                        <div className="card-icon">
                            <FaUserShield />
                        </div>
                        <h3>Create New Admin</h3>
                        <p>Grant admin privileges to users</p>
                    </div>
                </Link>
            </div>
        </div>
    );
}

export default AdminDashboard;