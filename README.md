# 📱 Spring Boot + Telegram WebApp Auth

Это проект на **Spring Boot**, который использует авторизацию через Telegram WebApp, сохраняет пользователей в базе данных и отображает их данные в веб-интерфейсе (с помощью Thymeleaf).

---

## 🚀 Основные функции

✅ Авторизация пользователей через Telegram  
✅ Сохранение и обновление данных пользователя  
✅ Отображение личного кабинета пользователя  
✅ Frontend на Bootstrap  
✅ Поддержка Spring Security с кастомной аутентификацией

---

## 📦 Стек технологий

- **Backend**: Java, Spring Boot, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap
- **База данных**: PostgreSQL
- **Интеграция**: Telegram WebApp

---

## ⚙️ Установка и запуск

1️⃣ **Клонируй проект**
```bash
git clone https://github.com/venyaka/SpringTelegramWebApp.git
cd SpringTelegramWebApp
```

2️⃣ **Подставь свои TELEGRAM_BOT_TOKEN и NGROK_URL в `credentials.env`**
```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=12345
POSTGRES_DB=spring_telegram

PGADMIN_DEFAULT_EMAIL=admin@admin.com
PGADMIN_DEFAULT_PASSWORD=root

TELEGRAM_BOT_TOKEN=<YOUR_TELEGRAM_BOT_TOKEN>
NGROK_URL=<YOUR_NGROK_URL>
```

3️⃣ **Настрой application.properties (если нужно)**
```
# application.properties
spring.application.name=spring_telegram
spring.config.import=optional:file:credentials.env[.properties]

spring.datasource.url=jdbc:postgresql://localhost:9876/${POSTGRES_DB}?createDatabaseIfNotExist=true
spring.datasource.username=${POSTGRES_USER}
spring.datasource.password=${POSTGRES_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

server.port=8181

logging.level.org.springframework.security=DEBUG

spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update

telegram.bot.token=${TELEGRAM_BOT_TOKEN}

ngrok.url=${NGROK_URL}
```

4️⃣ **Запусти Docker контейнеры**
> Убедись, что установлен Docker и Docker Compose.
```bash
cd .\docker-compose-file\
docker-compose --env-file ../credentials.env up -d --build
```
5️⃣ **Важно!** Для работы авторизации через Telegram:

* Перейдите в BotFather
* Откройте → Bot Settings → Domain → Edit domain
* Вставьте ваш актуальный публичный URL (ngrok-адрес)

📍 **Где найти ngrok-адрес?**  
После запуска проекта, откройте главную страницу сайта → `/` → там отображается актуальный ngrok URL.


---

## 📲 Как работает авторизация

1. Пользователь нажимает кнопку Telegram login widget.
2. Telegram передаёт данные (`user` объект) в endpoint `/authorize/login`.
3. Сервер проверяет подпись (валидность данных), создаёт/обновляет запись в базе.
4. Через `SecurityContextHolder` выставляется `Authentication`.
5. Пользователя редиректит на `/`, где отображаются его данные.

---

## 🔑 Основные endpoints

| Endpoint           | Описание                                      |
|--------------------|----------------------------------------------|
| `/authorize/login` | Страница с кнопкой Telegram login             |
| `/authorize/login` | POST endpoint для приёма данных Telegram      |
| `/`                | Личный кабинет авторизованного пользователя  |

---

## 🤝 Автор

Разработано https://github.com/venyaka.