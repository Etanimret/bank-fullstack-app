# Full Stack Application

This project is a full-stack application built with Java Spring Boot for the backend and Angular for the frontend. It demonstrates a simple setup where the backend serves data to the frontend.

## Project Structure

```
fullstack-app
├── backend          # Backend Spring Boot application
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── example
│   │   │   │           └── app
│   │   │   │               ├── Application.java
│   │   │   │               ├── controller
│   │   │   │               │   └── SampleController.java
│   │   │   │               └── service
│   │   │   │                   └── SampleService.java
│   │   │   └── resources
│   │   │       └── application.properties
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── example
│   │                   └── app
│   │                       └── ApplicationTests.java
│   ├── pom.xml
│   └── README.md
├── frontend         # Frontend Angular application
│   ├── src
│   │   ├── app
│   │   │   ├── app.component.ts
│   │   │   └── app.module.ts
│   │   └── index.html
│   ├── angular.json
│   ├── package.json
│   └── README.md
├── docker-compose.yml # Docker Compose configuration
└── README.md        # Overall project documentation
```

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- Node.js and npm
- Docker (optional, for containerized setup)

### Backend Setup

1. Navigate to the `backend` directory.
2. Run the following command to build the backend application:
   ```
   mvn clean install
   ```
3. Start the backend application:
   ```
   mvn spring-boot:run
   ```
4. The backend will be available at `http://localhost:8080`.

### Frontend Setup

1. Navigate to the `frontend` directory.
2. Install the dependencies:
   ```
   npm install
   ```
3. Start the Angular application:
   ```
   ng serve
   ```
4. The frontend will be available at `http://localhost:4200`.

### Docker Setup

To run the application using Docker, ensure you have Docker and Docker Compose installed. Then, run the following command in the root directory of the project:

```
docker-compose up
```

This will build and start both the backend and frontend services in Docker containers.

## Contributing

Feel free to submit issues or pull requests for improvements or bug fixes.

## License

This project is licensed under the MIT License.