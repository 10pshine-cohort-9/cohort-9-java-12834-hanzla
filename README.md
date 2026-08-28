# Contact Management System

A full-stack Contact Management System built with **Spring Boot, Spring Security, React, and MySQL**.

The application provides secure authentication, user-specific contact management, search, sorting, pagination, favorites, profile management, REST APIs, API documentation, validation, and automated backend testing.

---

## 🚀 Features

### 🔐 Authentication & Security

- User registration and login
- Session-based authentication
- Secure password hashing using BCrypt
- Spring Security authentication and authorization
- CSRF protection
- CORS configuration
- Secure logout
- Protected API endpoints
- Server-side request validation
- User-specific data access

### 👤 Contact Management

- Create contacts
- View contacts
- Update contacts
- Delete contacts
- Mark and unmark contacts as favorites
- Search contacts
- Sort contacts
- Paginate contacts
- Email type management
- Phone type management
- Input validation

### 📊 Dashboard

- Total contact statistics
- Favorite contact statistics
- User-related information
- Quick access to contact management functionality

### 👨‍💼 User Management

- User profile management
- Change password
- Secure authentication flow
- User-specific contact management

### ⚙️ Backend & API

- RESTful API architecture
- Layered backend architecture
- DTO-based request and response handling
- Service-layer business logic
- Repository-based data access
- Global exception handling
- Bean Validation
- Swagger / OpenAPI documentation
- Secure session management

### 🎨 Frontend

- React + Vite
- Responsive user interface
- Contact management interface
- Dashboard interface
- Profile management
- Form validation
- Loading states
- Error handling
- API integration using Axios
- React Icons
- Framer Motion animations

### 🧪 Testing

- Automated backend tests
- Unit tests
- Controller/API tests
- Validation testing
- Security-related testing

---

## 🛠️ Tech Stack

### Backend

| Technology | Purpose |
|---|---|
| Java | Backend programming language |
| Spring Boot | Backend framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| MySQL | Relational database |
| Maven | Dependency management and build |
| Bean Validation | Server-side validation |
| Swagger / OpenAPI | API documentation |

### Frontend

| Technology | Purpose |
|---|---|
| React | Frontend UI |
| Vite | Frontend build tool |
| JavaScript | Application logic |
| HTML | Structure |
| CSS | Styling |
| Axios | HTTP/API communication |
| React Icons | Icons |
| Framer Motion | UI animations |

### Development Tools

- Git
- GitHub
- VS Code
- IntelliJ IDEA
- Postman

---

## 🏗️ Architecture

The application follows a layered full-stack architecture.

```text
                         ┌──────────────────────┐
                         │       React UI       │
                         │      (Vite)          │
                         └──────────┬───────────┘
                                    │
                                    │ HTTP / REST
                                    ▼
                         ┌──────────────────────┐
                         │   Spring Security   │
                         │ Authentication/CSRF │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │     Controllers      │
                         │      REST APIs       │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │      Services       │
                         │  Business Logic     │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │    Repositories      │
                         │    Spring Data JPA  │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │       MySQL          │
                         │      Database        │
                         └──────────────────────┘