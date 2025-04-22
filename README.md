# 🎌 Anime Fan Community Platform

A full-stack **Spring Boot + Thymeleaf + WebSocket** application where anime enthusiasts can:
- Browse anime and character details.
- Participate in real-time discussions.
- Rate their favorite series and characters.
- Chat live using WebSockets.

---

## 📦 Features

### 🧠 User Functionality
- **Register/Login** with secure Spring Security.
- **JWT-less Session Authentication** (uses server sessions).
- **View Profile** and participate in anime/character discussions.

### 📺 Anime & Character Browsing
- Dynamic **Anime Listing** with pagination and genre filters.
- Detailed **Anime & Character Pages** with stats, image thumbnails, and discussions.

### 💬 Real-time Chat & Discussions
- Users can create new discussions (categorized by Anime/Character).
- **Live WebSocket Chat** on every discussion page.
- Seamless integration using STOMP and SockJS.

### 🗄️ Admin Panel (Optional)
- View/manage users, discussions, and messages.

---

## 🛠️ Tech Stack

| Layer         | Technology                          |
|---------------|-------------------------------------|
| Frontend      | Thymeleaf, Bootstrap 5, JS, CSS     |
| Backend       | Spring Boot 3, Spring Security      |
| Messaging     | WebSocket (STOMP, SockJS)           |
| Persistence   | JPA (Hibernate) + MySQL             |
| Auth          | Spring Security Session Auth        |

---

## 🚀 Setup Instructions

### 1. 📂 Clone the Repo
```bash
git clone https://github.com/AnujXIV/Anime-Discussion-Platform
cd anime-fan-community
```

### 2. ⚙️ Configure MySQL Database
- Create a schema `anime_db`
- Add your MySQL username/password in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/anime_db
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASS
```

### 3. 🧪 Seed Sample Data
The project auto-seeds 20 animes, characters, and default discussions on first run via `DataSeeder.java`.

### 4. ▶️ Run the App
```bash
./mvnw spring-boot:run
```

Visit: `http://localhost:8080/login`

---

## 🧪 Default Credentials

| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin    | USER  |


---

## 📁 Project Structure

```bash
src/
├── controller/               # UI Controllers for pages
├── rest/                     # REST APIs
├── websocket/                # WebSocket Message Mappings
├── service/                  # Business logic services
├── model/                    # JPA Entities
├── repository/               # JPA Repositories
├── config/                   # Spring Security & WebSocket Configs
├── templates/                # Thymeleaf HTML templates
├── static/                   # JS, CSS, images
└── AnimeFanCommunityApplication.java
```

---

## 🙌 Contributors

- [Anujkumar Patel](https://github.com/AnujXIV)
- [Arjav Patel](https://github.com/ArjavJP)
- [Darshan Gandhi]()
- [Hardik Shah]()

---

## 📄 License

MIT License © 2025 Anime Fan Community