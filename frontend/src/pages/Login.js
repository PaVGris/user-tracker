import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css'; // подключаем стили
function Login({ onLogin }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        setError(''); // очищаем ошибку при новом сабмите
        const res = await fetch('http://localhost:8081/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });

        if (res.ok) {
            const data = await res.json();
            sessionStorage.setItem('token', data.token);
            localStorage.setItem('username', username);
            onLogin(data.token);
            navigate('/welcome');
        } else {
            setError('Неверный логин или пароль');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Логин</h2>
            <input placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} />
            <input placeholder="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} />
            <button type="submit">Login</button>

            {error && <p className="error-message">{error}</p>}

            <button
                type="button"
                className="secondary-button"
                onClick={() => navigate('/register')}
            >
                Ещё нет аккаунта? Зарегистрироваться
            </button>
        </form>
    );
}

export default Login;
