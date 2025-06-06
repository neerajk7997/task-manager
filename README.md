
# Task Manager Microservice

## Setup

### Backend
- Java 17, Spring Boot 3
- mvn clean spring-boot:run
- Run `./mvnw clean package`
- Run `docker build -t task-manager .`
- Run `docker run -p 8080:8080 task-manager`

### Frontend
- Angular CLI project
- Filter dropdown added in `upcoming-tasks.component.ts`

### Lambda
- Simple Java program using `HttpURLConnection`

## Example API Requests
- GET `/api/v1/tasks?dueBefore=2025-06-06&priority=HIGH&completed=false`

## SQL DDL
See `schema/tasks.sql`


# Build JAR
./mvnw clean package

# Build Docker image
docker build -t task-manager .

# Run Docker container locally
docker run -p 8080:8080 task-manager

# Push Docker image to DockerHub (replace your-dockerhub-username)
docker tag task-manager your-dockerhub-username/task-manager:latest
docker push your-dockerhub-username/task-manager:latest
