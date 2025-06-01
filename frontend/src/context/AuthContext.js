// src/context/AuthContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import { checkAuth } from '../api/authService';

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [currentUser, setCurrentUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const validateToken = async () => {
            try {
                const userData = await checkAuth();
                setCurrentUser(userData);
            } catch (error) {
                localStorage.removeItem('token');
            } finally {
                setLoading(false);
            }
        };

        validateToken();
    }, []);

    const login = async (token) => {
        localStorage.setItem('token', token);
        try {
            const userData = await checkAuth();
            setCurrentUser(userData);
        } catch (error) {
            logout();
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        setCurrentUser(null);
    };

    const value = {
        currentUser,
        login,
        logout,
        loading
    };

    return (
        <AuthContext.Provider value={value}>
            {!loading && children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}