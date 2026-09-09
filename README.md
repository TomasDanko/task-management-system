# Task Management System

Backend application for managing users, projects, tasks and comments.

I created this project as a portfolio project to practice and demonstrate my experience with Java, Spring Boot, REST API, database work and Spring Security with JWT.

## Features

* User registration and login
* JWT authentication
* User roles
* Project management
* Task management
* Assigning tasks to users
* Task statuses
* Comments
* Audit log
* Filtering and pagination
* Input validation
* Global exception handling
* REST API
* Swagger / OpenAPI
* Unit and integration tests

## Technologies

* Java 17
* Spring Boot 3.5
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL
* H2
* MapStruct
* Maven
* JUnit
* Mockito
* Swagger / OpenAPI
* Docker
* Git

## Project structure

The application is divided into several layers:

* Controller – REST endpoints
* Service – business logic
* Repository – database access
* Entity – database entities
* DTO – data transfer objects
* Mapper – mapping between entities and DTOs
* Security – JWT authentication and authorization
* Exception – custom exceptions and global exception handling

## Main entities

The application currently works with:

* User
* Project
* Task
* Comment
* AuditLog

## Authentication

Registration and login are handled using Spring Security and JWT.

After successful login, the API returns a JWT token which is then used to access protected endpoints.

### Authentication endpoints

```http
POST /api/user/register
POST /api/user/login
GET  /api/user/currentUser
GET  /api/user/users
```

* `POST /api/user/register` – registration of a new user
* `POST /api/user/login` – login and JWT token generation
* `GET /api/user/currentUser` – returns the currently authenticated user
* `GET /api/user/users` – returns all users and is restricted by role

JWT token is sent in the request header:

```http
Authorization: Bearer <token>
```

All other protected endpoints require a valid JWT token.

## API examples

### Users

```http
GET    /api/users
GET    /api/users/{id}
PUT    /api/users/{id}
DELETE /api/users/{id}
```

### Projects

```http
GET    /api/projects
GET    /api/projects/{id}
POST   /api/projects
PUT    /api/projects/{id}
DELETE /api/projects/{id}
```

### Tasks

```http
GET    /api/tasks
GET    /api/tasks/{id}
POST   /api/tasks
PUT    /api/tasks/{id}
DELETE /api/tasks/{id}
```

The API also contains endpoints for filtering tasks, changing task status, assigning tasks and getting tasks by project or user.

### Comments

```http
GET    /api/comments
GET    /api/comments/{id}
POST   /api/comments
PUT    /api/comments/{id}
DELETE /api/comments/{id}
```

### Audit log

The application stores information about important operations performed in the system.

## Database

The project uses PostgreSQL as the main database.

H2 is used for tests.

The database schema is created automatically by Hibernate based on the JPA entities.

## Testing

The project contains tests for:

* Services
* Controllers
* Repositories
* Mappers
* JWT authentication
* Validation
* Integration flow

JUnit and Mockito are used for testing.

## Running the application

### Requirements

* Java 17
* Maven
* PostgreSQL

Clone the repository and open it in IntelliJ IDEA.

Configure the database connection in:

```text
src/main/resources/application.properties
```

Then run the application from IntelliJ or using Maven:

```bash
mvn spring-boot:run
```

The application runs by default on:

```text
http://localhost:8080
```

## Swagger

After starting the application, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

It can be used to test the REST API directly from the browser.

## Author

Tomáš Danko

GitHub: TomasDanko
