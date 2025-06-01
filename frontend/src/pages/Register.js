import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Register.css'; // подключаем стили
function Register() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [url, setUrl] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Проверка на пустые поля
        if (!username.trim() || !password.trim() || !url.trim()) {
            alert('Все поля обязательны для заполнения');
            return;
        }

        const res = await fetch('http://localhost:8081/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password, url }),
        });

        if (res.ok) {
            alert('Успешная регистрация');
            navigate('/login');
        } else {
            alert('Ошибка регистрации');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Регистрация</h2>
            <input
                placeholder="Username"
                value={username}
                onChange={e => setUsername(e.target.value)}
            />
            <input
                placeholder="Password"
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
            />
            <input
                placeholder="Website"
                value={url}
                onChange={e => setUrl(e.target.value)}
            />
            <button type="submit">Register</button>
            <button
                type="button"
                className="secondary-button"
                onClick={() => navigate('/login')}
            >
                Уже есть аккаунт? Войти
            </button>
        </form>
    );
}

export default Register;