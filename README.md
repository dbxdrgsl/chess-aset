# Chess-ASET: Electronic Chess Board

**Advanced Software Engineering Project**

An intelligent electronic chess board system combining physical hardware with a modern Spring Boot backend for game management, authentication, and real-time chess playing.

---

## 🎯 Project Overview

Chess-ASET is a full-stack project that integrates:
- 🎲 **Physical chess board** with magnetic sensors and LED indicators
- 🖥️ **Spring Boot backend** for game logic and user management
- 🔐 **JWT authentication** for secure access
- 📊 **PostgreSQL database** for game state persistence
- 📡 **RESTful API** with OpenAPI/Swagger documentation

---

## 📚 Documentation

- **[Technical Analysis Report](TECHNICAL_ANALYSIS_REPORT.md)** - Comprehensive 30-section analysis
- **[Technical Summary](TECHNICAL_ANALYSIS_SUMMARY.md)** - Quick reference guide
- **[Notion Documentation](https://better-trail-af2.notion.site/Electronic-Chess-Board-Advanced-Software-Engineering-Project-2868e349b23c802cb616d98cfd392cb1?source=copy_link)** - Project planning and requirements
- **[Trello Board](https://trello.com/b/CiWF1fLf)** - Task management
- **[Design Document](https://docs.google.com/document/d/1Kk508ZuEWHZN0voQITxZBIbCEiYkDmgAplRw2pLtEI8/edit?tab=t.0)** - Detailed specifications

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- PostgreSQL 13+
- Maven 3.6+
- (Optional) MicroPython-compatible board for hardware

### Running the Backend

```bash
cd "Lates Version/demo"
mvn spring-boot:run
```

The server will start on http://localhost:8081

### API Documentation
- Swagger UI: http://localhost:8081/swagger-ui.html
- API Docs: http://localhost:8081/v3/api-docs

### Database Setup
```sql
CREATE DATABASE Chess_Ip;
```
Update credentials in `src/main/resources/application.properties`

---

## 🏗️ Architecture

```
┌─────────────────────┐
│  Physical Hardware  │  ← MicroPython + I2C Sensors
│  (Electronic Board) │
└──────────┬──────────┘
           │ HTTP/JSON
           ▼
┌─────────────────────┐
│   Spring Boot API   │  ← Java 17 + REST
│  (Backend Server)   │
└──────────┬──────────┘
           │ JPA/Hibernate
           ▼
┌─────────────────────┐
│    PostgreSQL DB    │  ← Game State Storage
└─────────────────────┘
```

---

## 💻 Technology Stack

**Backend:** Spring Boot 3.5.7, Java 17, PostgreSQL, JWT  
**Hardware:** MicroPython, I2C, MCP23017 GPIO expanders  
**Libraries:** chesslib 1.3.4, Lombok, SpringDoc OpenAPI  
**Build Tool:** Maven

---

## 📁 Project Structure

- `FirstComponents/` - Full-featured backend implementation
- `Lates Version/` - Latest iteration with improvements *(directory name has typo)*
- `Gamer Server/` - Simplified game server
- `RepositoryClass/` - Data access layer examples
- `SecurityClass/` - Security implementation and tests
- `Hardware/` - MicroPython embedded code

---

## 🎓 Key Features

✅ Physical chess board with magnetic piece detection  
✅ Automatic chess rule validation  
✅ RESTful API for game management  
✅ JWT-based authentication  
✅ Game lobby and matchmaking  
✅ Move history tracking  
✅ OpenAPI/Swagger documentation  
✅ PostgreSQL with JSONB support  

---

## 📊 Project Statistics

- **132** Java files (~5,300 LOC)
- **10** Python files (~300 LOC)
- **Spring Boot** microservices architecture
- **MIT License** - Open source

---

## 📖 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Game Management
- `POST /api/games/moves` - Execute a chess move
- `GET /api/games/{gameId}` - Get current game state

### Game Lobby
- Create and join game lobbies
- Player matchmaking

See [Swagger UI](http://localhost:8081/swagger-ui.html) for complete API documentation.

---

## 🔧 Development

### Build Project
```bash
mvn clean install
```

### Run Tests
```bash
mvn test
```

### Database Migration
Currently using `hibernate.ddl-auto=create-drop` (development mode)

---

## 🤝 Contributing

This is an academic project for Advanced Software Engineering. See technical analysis documents for improvement suggestions.

---

## 📄 License

MIT License - See [LICENSE](LICENSE) file for details.

Copyright (c) 2025 dragoș

---

## 📞 Support

For questions or issues, please refer to the documentation links above or contact the project maintainers.

---

**Status:** Early Development / Prototype  
**Last Updated:** January 16, 2026
