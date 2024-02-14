# Artsoft Backend

Artsoft Backend is a backend system designed for e-commerce stores, 
aimed at providing robust user management and security features. 
This system supports essential functionalities such as user registration, 
secure login processes, email verification, and password resetting, ensuring secure access to all endpoints. 
Built with Spring Boot, Artsoft Backend leverages PostgreSQL for database management, 
Hibernate as the ORM tool for efficient database operations, 
smtp4dev for email testing in development environments, and Swagger UI for interactive API documentation.

Frontend project is available at [artsoft-frontend](https://github.com/wolskimarcin/artsoft-frontend).


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Prerequisites

Ensure Docker and Docker Compose are installed on your system before proceeding.

### Cloning the Repository

First, clone the repository to your local machine. Open a terminal and run the following command:

```bash
git clone https://github.com/wolskimarcin/artsoft-backend.git
```

## Setting Up the Development Environment

To set up your development environment, simply run the `docker-compose.yml` file. This approach eliminates the need for manually creating and managing containers.

Run the following command:

```bash
docker-compose up -d
```

This command orchestrates the setup of your entire development environment, including the PostgreSQL database and smtp4dev, as defined in the `docker-compose.yml`.

### Components

#### PostgreSQL Database

The PostgreSQL service is configured in `docker-compose.yml`. It is automatically set up with the necessary environment variables (`POSTGRES_USER` and `POSTGRES_PASSWORD`) and port mappings.

#### smtp4dev

smtp4dev is configured to simulate an SMTP server, aiding in the development and testing of email functionalities. It's also set up through `docker-compose.yml`, making it readily accessible without additional commands.

- **Accessing the smtp4dev Web Interface**:

  Open your web browser and navigate to `http://localhost:3000/` to interact with the smtp4dev interface.

#### Swagger UI

Swagger UI is integrated for easy documentation and testing of REST API endpoints.

- **Accessing Swagger UI**:

  Open your web browser and go to `http://localhost:8080/swagger-ui/index.html` to view and test your API's endpoints.

### Managing Your Environment

- **Starting Services**: If your services are not running, start them with `docker-compose up -d`.

- **Stopping Services**: To stop all services, use `docker-compose down`. For stopping without removing containers, networks, etc., use `docker-compose stop`.

- **Restarting Services**: To restart any service, use `docker-compose restart <service_name>`. For a complete restart of all services, you can run `docker-compose down` followed by `docker-compose up -d`.
