import React, { useEffect, useRef, useState } from 'react';
import h337 from 'heatmap.js';
import {useNavigate} from "react-router-dom";

const HeatmapPage = () => {
    const navigate = useNavigate();
    const [imageUrl, setImageUrl] = useState(null);
    const [clicks, setClicks] = useState([]);
    const heatmapInstanceRef = useRef(null);
    const containerRef = useRef(null);
    const [pagePath, setPagePath] = useState('');
    const [clientId, setClientId] = useState('1');
    const token = sessionStorage.getItem('token');

    const fetchHeatmapData = async () => {
        if (!clientId || !pagePath) return;

        const response = await fetch(`http://localhost:8081/data/heatmap/${clientId}/index`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            },
        });
        const data = await response.json();
        setClicks(data);
    };

    useEffect(() => {
        if (containerRef.current && clicks.length > 0) {
            if (heatmapInstanceRef.current) {
                heatmapInstanceRef.current.setData({
                    max: 4,
                    data: clicks
                });
            } else {
                heatmapInstanceRef.current = h337.create({
                    container: containerRef.current,
                    radius: 30,
                    maxOpacity: 1,
                    minOpacity: 0.3,
                    blur: 0.5,
                    gradient: {
                        '.3': 'blue',
                        '.5': 'cyan',
                        '.7': 'lime',
                        '.95': 'yellow',
                        '1': 'red'
                    }
                });

                heatmapInstanceRef.current.setData({
                    max: 5,
                    data: clicks
                });
            }
        }
    }, [clicks]);

    const handleUpload = async (e) => {
        const file = e.target.files[0];
        if (file) {
            setImageUrl(URL.createObjectURL(file));
        }
    };

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            padding: '10px',
            margin: '0 auto'
        }}>
            <h2 style={{ fontSize: '32px', fontWeight: 'bold' }}>Просмотр тепловой карты</h2>

            <div style={{ marginBottom: '20px', display: 'flex', gap: '10px' }}>
                <button
                    onClick={() => navigate('/welcome')}
                    style={{
                        padding: '8px 16px',
                        backgroundColor: '#6a5acd',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '14px'
                    }}
                >
                    Вернуться
                </button>

                <input
                    type="text"
                    placeholder="Page Path (e.g., /about)"
                    value={pagePath}
                    onChange={(e) => setPagePath(e.target.value)}
                    style={{ padding: '8px' }}
                />
                <input
                    type="file"
                    accept="image/png"
                    onChange={handleUpload}
                    style={{ padding: '8px' }}
                />

                <button
                    onClick={fetchHeatmapData}
                    style={{
                        padding: '8px 16px',
                        backgroundColor: '#4CAF50',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer'
                    }}
                >
                    Сформировать
                </button>
            </div>

            {imageUrl && (
                <div
                    ref={containerRef}
                    style={{
                        position: 'relative',
                        display: 'inline-block',
                        width: '100%',
                        maxWidth: '800px',
                        marginTop: '20px'
                    }}
                >
                    <img
                        src={imageUrl}
                        alt="Page"
                        style={{
                            width: '100%',
                            opacity: 0.4,
                            display: 'block',
                            position: 'relative',
                            zIndex: 1,
                            border: '1px solid #ccc'
                        }}
                    />
                </div>
            )}
        </div>
    );
};

export default HeatmapPage;