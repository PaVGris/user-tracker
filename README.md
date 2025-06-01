# User Tracker

Разработка системы мониторинга и анализа клиентского поведения для улучшения удобства использования веб-приложения

##  Быстрый старт с Docker Compose

### Предварительные требования
- Установленный [Docker](https://docs.docker.com/engine/install/)
- Установленный [Docker Compose](https://docs.docker.com/compose/install/)
- Порты 3001 (frontend), 5000 (backend)

## Установка и запуск

### Клонируйте репозиторий:
```bash
git clone https://github.com/PaVGris/user-tracker.git
cd user-tracker
```

## Структура проекта
| Компонент       | Файл               | Технологии       | Описание                |
|-----------------|--------------------|------------------|-------------------------|
| Основная инфра  | `docker-compose.yml` | Kafka + ZooKeeper | Брокер сообщений |
| Фронтенд        | `frontend/docker-compose.yml` | Grafana + React  | Мониторинг и интерфейс  |
| Хранилище 1     | `clickhouse/docker-compose.yml` | ClickHouse       | Аналитическое хранилище |
| Хранилище 2     | `postgres/docker-compose.yml` | PostgreSQL       | Реляционная БД          |

## Запуск системы

### 1. Установите зависимости
- [Docker](https://docs.docker.com/engine/install/)
- [Node.js](https://nodejs.org/) (для фронтенда)

### 2. Запустите сервисы

2.1 Kafka и ZooKeeper
```bash
cd user-tracker
docker-compose up -d
```

2.2 PostgreSQL
```bash
cd postgres
docker-compose up -d
```

2.3 ClickHouse
```bash
cd ../clickhouse
docker-compose up -d
```

2.4 Grafana
```bash
cd ../frontend
docker-compose up -d
```

2.5 Frontend (в новом терминале)
```bash
npm install
npm run dev
```
