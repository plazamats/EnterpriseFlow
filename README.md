# EnterpriseFlow

EnterpriseFlow is a multi-tenant IT service desk and asset-management platform.

## Current foundation

- Java 21 / Spring Boot 3.4
- PostgreSQL with Flyway migrations
- Stateless Spring Security 6 JWT access tokens
- Hashed, rotated refresh tokens
- BCrypt password hashing and account lockout after repeated failures
- Organisation-scoped user model
- React/TypeScript
- Docker Compose deployment

## Run locally

1. Start PostgreSQL: `docker compose up -d postgres`
2. Set `JWT_SECRET` using `.env.example`.
3. Run the API: `cd backend; mvn spring-boot:run`
4. Run the web app: `cd frontend; npm install; npm run dev`

