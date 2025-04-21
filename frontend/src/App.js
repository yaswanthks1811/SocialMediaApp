// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './auth/Login'; // Corrected path
import Feed from './pages/Feed';
import Navbar from './components/NavBar';
import Profile from './pages/Profile';
import Search from './pages/Search';
import CreatePost from './pages/CreatePost';
import Notifications from './pages/Notification';
import Recommendations from './pages/Recommendations';
import ViewMessages from './pages/ViewMessage';
import MessageInbox from './pages/MessageInbox';
import Trending from './pages/Trending';
import ViewFollowers from './pages/ViewFollowers';
import ViewFollowing from './pages/ViewFollowing';
import RegisterPage from './auth/Register';
import EditProfile from './pages/EditProfile';
import AdminDashboard from './admin/AdminDashBoard';
import CreateAdmin from './admin/CreateAdmin';
import ManagePosts from './admin/ManagePosts';
import ManageUsers from './admin/ManageUsers';
import AdminLogin from './auth/AdminLogin';
import DisplayPost from './components/DisplayPost';

function App() {
    return (
        <AuthProvider>
          
            <Router>
                <Routes>
                    <Route path="/" element={<LoginPage />} />
                    <Route path='/register' element={<RegisterPage />} />
                    <Route path='/feed' element={<Feed />}/>
                    <Route path='/trending' element={<Trending />}/>
                    <Route path='/profile' element={<Profile />}/>
                    <Route path='/editProfile' element={<EditProfile />}/>
                    <Route path='/viewFollowers' element={<ViewFollowers />}/>
                    <Route path='/viewFollowing' element={<ViewFollowing />}/>
                    <Route path='/search' element={<Search />}/>
                    <Route path='/uploadPost' element={<CreatePost />}/>
                    <Route path='/notifications' element={<Notifications />}/>
                    <Route path='/recommendations' element={<Recommendations />}/>
                    <Route path='/inbox' element={<MessageInbox />}/>
                    <Route path='/viewMessages' element={<ViewMessages />}/>
                    <Route path='/admin' element={<AdminDashboard />}/>
                    <Route path='/admin/createAdmin' element={<CreateAdmin/>}/>
                    <Route path='/admin/posts' element={<ManagePosts />}/>
                    <Route path='/admin/users' element={<ManageUsers />}/>
                    <Route path="/admin/post/:postId" element={<DisplayPost />} />
                    <Route path='/admin/login' element={<AdminLogin />}/>
                    <Route path="*" element={<Navigate to="/" />} />
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
