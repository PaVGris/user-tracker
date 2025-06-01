import {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import './Welcome.css'; // подключаем стили
function Welcome({onLogout}) {
    const [siteName, setSiteName] = useState(''); // Заменить на имя сайта пользователя
    const navigate = useNavigate();
    const [isCopied, setIsCopied] = useState(false);
    const username = localStorage.getItem('username'); // Получаем имя пользователя из localStorage
    const token = sessionStorage.getItem('token'); // Получаем токен из localStorage// Получаем имя пользователя из localStorage


    const trackerCode = `<script src="http://localhost:8081/tracker.js" data-user-id==1></script>`;

    useEffect(() => {
        // Делаем запрос для получения URL
        const fetchUrl = async () => {
            try {
                const res = await fetch(`http://localhost:8081/data/get-url/${username}`, {
                    headers: {
                        'Authorization': `Bearer ${token}` // Добавляем токен в заголовок
                    }
                });

                if (res.ok) {
                    const text = await res.text(); // Читаем тело как текст (если это URL)
                    // Пытаемся использовать полученный текст (URL)
                    setSiteName(text);
                } else {
                    console.error("Ошибка при получении URL", res.statusText);
                }
            } catch (error) {
                console.error("Ошибка при запросе:", error);
            }
        };

        if (username) {
            fetchUrl().then(r => {});
        }
    }, [username]);

    const handleHeatmapClick = () => {
        navigate('/heatmap'); // Переход на страницу Heatmap
    };

    const handleCopyTracker = () => {
        navigator.clipboard.writeText(trackerCode)
            .then(() => {
                setIsCopied(true);
                setTimeout(() => setIsCopied(false), 2000);
            })
            .catch(err => {
                console.error('Не удалось скопировать трекер: ', err);
            });
    };

    return (
        <div className="analytics-container">
            <header className="header">
                <div className="header-left">
                    <div className="site-name">{siteName}</div>
                    <span
                        className="tracker-link"
                        onClick={handleCopyTracker}
                        title="Копировать код трекера"
                    >
                        Получить трекер
                        {isCopied && <span className="copy-message">Код трекера скопирован!</span>}
                    </span>
                </div>
                <button className="heatmap-button" onClick={handleHeatmapClick}>
                    Heatmap
                </button>
            </header>

            <div className="grafana-chart">
                {/* Здесь будет график из Grafana */}
                <iframe
                    src="http://localhost:3000/d-solo/dekuwy8flbrb4e/new-dashboard?orgId=1&timezone=browser&refresh=2h&theme=light&panelId=1&__feature.dashboardSceneSolo"
                    width="450" height="200" frameBorder="0"></iframe>
            </div>

            <div className="columns">
                <div className="left-column">
                    <h3>Статистика сайта</h3>
                    <ul>
                        <li>Среднее время на сайте: 5 мин</li>
                        {/* Здесь нужно динамически заполнять данные */}
                        <li>Среднее количество пользователей: 1200</li> {/* Здесь нужно динамически заполнять данные */}
                        <li>Активность пользователей: 87%</li> {/* Здесь нужно динамически заполнять данные */}
                    </ul>
                </div>

                <div className="right-column">
                    <h3>Топ сайтов</h3>
                    <ul>
                        <li>/home: 12k</li>
                        <li>/about: 8k</li>
                        <li>/contact: 5k</li>
                        {/* Здесь нужно динамически заполнять данные */}
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default Welcome;
