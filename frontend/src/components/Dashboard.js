// src/components/Dashboard.js
import React from 'react';
import { useAuth } from '../context/AuthContext';

function Dashboard() {
    const { currentUser, logout } = useAuth();

    return (
        <div className="dashboard">
            <h1>Welcome to your Dashboard, {currentUser?.username}!</h1>
            <p>This is a protected page that only authenticated users can see.</p>

            <div className="user-info">
                <h2>Your Information:</h2>
                <p><strong>Username:</strong> {currentUser?.username}</p>
            </div>

            <button
                onClick={logout}
                className="logout-btn"
            >
                Logout
            </button>
        </div>
    );
}

export default Dashboard;