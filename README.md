# Todo List App

A full-stack task management application built with Spring Boot, PostgreSQL, and Vite React.

## Features

- Display todo list
- Add new todos
- Edit existing todos
- Delete todos
- Mark todos as completed or active
- Search todos by title or description
- Filter todos by status
- Responsive client UI
- Backend validation and structured error responses
- Seed data for local development

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Bean Validation

### Frontend

- React
- TypeScript
- Vite
- ESLint

## Project Structure

```text
todo-list-app/
  server/          Spring Boot REST API
  client/          Vite React client
  README.md        Setup and run instructions
```

## Prerequisites

Install these before running the project:

- Java 21
- Node.js and npm
- PostgreSQL

Maven does not need to be installed separately because the backend includes Maven Wrapper.

## Database Setup

Create a PostgreSQL database named `todolist`:

```sql
CREATE DATABASE todolist;
```

The backend uses this local configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=postgres
spring.datasource.password=123
```

Update `server/src/main/resources/application.properties` if your local PostgreSQL username or password is different.

## Run The Project

Start the backend:

```bash
cd server
./mvnw spring-boot:run
```

On Windows:

```bat
cd server
mvnw.cmd spring-boot:run
```

Start the frontend in another terminal:

```bash
cd client
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

The backend runs at:

```text
http://localhost:8080
```

## API Endpoints

Base URL:

```text
http://localhost:8080/api/todos
```

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/todos` | Get all todos |
| GET | `/api/todos?search=api` | Search todos |
| GET | `/api/todos?completed=true` | Filter completed todos |
| GET | `/api/todos/{id}` | Get todo by ID |
| POST | `/api/todos` | Create todo |
| PUT | `/api/todos/{id}` | Update todo |
| PATCH | `/api/todos/{id}/completed?completed=true` | Update completion status |
| DELETE | `/api/todos/{id}` | Delete todo |

## Example Request

```json
{
  "title": "Write README run steps",
  "description": "Document backend, frontend, and database setup."
}
```

## Validation

The backend validates incoming requests:

- `title` is required
- `title` must be at most 120 characters
- `description` must be at most 500 characters

Invalid requests return structured JSON error responses.

## Notes

On first run, the backend creates the `todos` table automatically and seeds sample todo records if the table is empty.
