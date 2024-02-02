# Artsoft Backend

Artsoft Backend is a backend system designed for e-commerce stores, 
aimed at providing robust user management and security features. 
This system supports essential functionalities such as user registration, 
secure login processes, email verification, and password resetting, ensuring secure access to all endpoints. 
Built with Spring Boot, Artsoft Backend leverages PostgreSQL for database management, 
Hibernate as the ORM tool for efficient database operations, 
smtp4dev for email testing in development environments, and Swagger UI for interactive API documentation.


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Before you begin, ensure you have Docker installed on your system.

### Setting Up the Development Environment

#### PostgreSQL Database

1. **Create a PostgreSQL container**:
    ```bash
    docker run --name artsoft-postgres-container -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -d -p 5432:5432 postgres
    ```

2. **Restart the PostgreSQL container** (if needed):
    ```bash
    docker restart artsoft-postgres-container
    ```

3. **Access the PostgreSQL container**:
    ```bash
    docker exec -it artsoft-postgres-container psql -U postgres
    ```

#### smtp4dev

smtp4dev is used to simulate an SMTP server for development purposes.

1. **Run smtp4dev in Docker**:
    ```bash
    docker run --rm -it -p 3000:80 -p 2525:25 rnwood/smtp4dev
    ```

2. **Access the smtp4dev web interface**:
   Open your web browser and navigate to:
    ```
    http://localhost:3000/
    ```

#### Swagger UI

Swagger UI is utilized for documenting and testing the REST API endpoints.

- **Access Swagger UI**:
  Open your web browser and navigate to:
    ```
    http://localhost:8080/swagger-ui/index.html
    ```
