// src/context/AuthContext.js
import React, { createContext, useState } from 'react';
import * as authService from '../../services/authService';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [userId, setUserId] = useState(authService.getCurrentUserId());

    const login = async (newUserId) => {
        setUserId(newUserId);
        localStorage.setItem('userId', newUserId);
    };

    const logout = async () => {
        await authService.logoutUser();
        setUserId(null);
    };

    const value = { userId, login, logout };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};