Gym Final Project EPAM
---

## Getting Started

### Prerequisites
- Download and install Java 17.
- Make sure all project dependencies are installed and up to date.

### Create ActiveMQ Docker Container

- First, pull the official ActiveMQ image from Docker Hub. Open your terminal or command prompt.
   ```bash
     docker pull rmohr/activemq
   ```
- Run a Docker container based on the ActiveMQ image, and map the required ports (61616 for messaging and 8161 for the web console):
 ```bash
     docker run -d -p 61616:61616 -p 8161:8161 --name activemq rmohr/activemq
 ```

### Setting Up
1. Run Docker Compose to create the necessary containers.
    ```bash
    docker-compose up -d
    ```

2. Ensure that the database and Prometheus containers are successfully created.

### Configuration
- Check the commit history to find the profiles for database properties and custom metrics for Prometheus.

### Running the Project
- After completing the setup steps above, you can now run the project.

---

To see Swagger UI, go to http://localhost:8080/doc/swagger-ui/
