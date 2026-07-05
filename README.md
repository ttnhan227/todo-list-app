# Todo List App

Live demo: https://todo-list-app-client-p26u.onrender.com
(The backend is hosted on Render free tier, so the first load may take a moment while the server wakes up.)

A full-stack task management application built with Spring Boot, PostgreSQL, and Vite React.

## Features

- Display todo list
- Add new todos
- Edit existing todos
- Delete todos
- Mark todos as completed or active
- Search todos by title or description
- Filter todos by status
- Paginate todos
- Responsive client UI
- Backend validation and structured error responses
- Daily task seed data for local development
- Docker setup for PostgreSQL, backend, and frontend
- Backend unit tests for service logic
- Deployment-ready environment configuration

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
  docker-compose.yml
  README.md        Setup and run instructions
```

## Prerequisites

Install these before running the project:

- Java 21
- Node.js and npm
- PostgreSQL
- Docker Desktop, optional for Docker setup

Maven does not need to be installed separately because the backend includes Maven Wrapper.

## Database Setup

Create a PostgreSQL database named `todolist`:

```sql
CREATE DATABASE todolist;
```

The backend uses this local configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=<your-postgres-username>
spring.datasource.password=<your-postgres-password>
```

Use the username and password from your local PostgreSQL setup.

The backend also supports environment variables:

```properties
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
APP_CORS_ALLOWED_ORIGINS
```

The frontend supports this environment variable:

```properties
VITE_API_BASE_URL
```

## Run The Project

Use these steps if you are not using Docker.

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

## Run With Docker

Start PostgreSQL, the Spring Boot API, and the React client:

```bash
docker compose up --build
```

Open:

```text
http://localhost:5173
```

The backend runs at:

```text
http://localhost:8080
```

Stop the containers:

```bash
docker compose down
```

Remove the database volume too:

```bash
docker compose down -v
```

Docker services:

- `db`: PostgreSQL database
- `server`: Spring Boot API on `http://localhost:8080`
- `client`: Vite React client on `http://localhost:5173`

## Run Tests

Run backend tests:

```bash
cd server
./mvnw test
```

On Windows:

```bat
cd server
mvnw.cmd test
```

The unit tests cover todo service pagination, invalid pagination input, todo creation cleanup, completion updates, and missing todo IDs.

## API Endpoints

Base URL:

```text
http://localhost:8080/api/todos
```

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/todos?page=0&size=5` | Get paginated todos |
| GET | `/api/todos?search=groceries` | Search todos |
| GET | `/api/todos?completed=true` | Filter completed todos |
| GET | `/api/todos?search=groceries&completed=false&page=0&size=5` | Search, filter, and paginate todos |
| GET | `/api/todos/{id}` | Get todo by ID |
| POST | `/api/todos` | Create todo |
| PUT | `/api/todos/{id}` | Update todo |
| PATCH | `/api/todos/{id}/completed?completed=true` | Update completion status |
| DELETE | `/api/todos/{id}` | Delete todo |

## Example Request

```json
{
  "title": "Buy groceries",
  "description": "Pick up vegetables, eggs, milk, and rice for the week."
}
```

## Validation

The backend validates incoming requests:

- `title` is required
- `title` must be at most 120 characters
- `description` must be at most 500 characters

Invalid requests return structured JSON error responses.

## Notes

On first run, the backend creates the `todos` table automatically and seeds 20 daily todo records if the table is empty.
