FROM postgres:latest

# Устанавливаем переменные окружения для настройки БД
ENV POSTGRES_DB=social_network
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=80958095

# Копируем скрипт инициализации БД в образ
COPY 001-create-schema.sql /docker-entrypoint-initdb.d/
COPY 002-fill-dialog-tables.sql /docker-entrypoint-initdb.d/

# Expose the PostgreSQL port
EXPOSE 5432