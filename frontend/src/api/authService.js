// src/api/authService.js
import api from './api';

export const loginUser = async (credentials) => {
    const response = await api.post('/auth/login', {
        username: credentials.username,
        password: credentials.password
    });
    return response.data;
};

// В вашем authService.js
export const registerUser = async (userData) => {
    const response = await fetch('http://localhost:8081/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include', // Важно для CORS с куками
        body: JSON.stringify({
            username: userData.username,
            password: userData.password
        }),
    });

    if (!response.ok) {
        throw new Error(await response.text());
    }

    return await response.json();
};

export const checkAuth = async () => {
    try {
        const response = await api.get('/auth/check');
        return response.data;
    } catch (error) {
        throw error;
    }
};