# Fullstack Application Backend

This is the backend part of the fullstack application built using Java Spring Boot.

## Prerequisites

- Java 11 or higher
- Maven
- Docker (optional, for containerization)

## Setup Instructions

1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd fullstack-app/backend
   ```

2. **Build the project**:
   ```
   mvn clean install
   ```

3. **Run the application**:
   ```
   mvn spring-boot:run
   ```

4. **Access the application**:
   The application will be running at `http://localhost:8080`.

## API Endpoints

- `GET /api/sample`: Returns sample data.

## Testing

To run the tests, use the following command:
```
mvn test
```

## Docker Setup

To run the backend in a Docker container, use the provided `docker-compose.yml` file in the root directory of the project.

## Additional Information

For more details on the application structure and functionality, refer to the source code in the `src` directory.