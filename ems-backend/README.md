# EMS Backend - Docker Setup

## Prerequisites

- Docker installed: [Get Docker](https://docs.docker.com/get-docker/)
- Docker Compose installed (usually included with Docker Desktop)

---

## Build and Run the Service with Dockerfile

1. **Build the Docker image:**
   ```sh
   docker build -t ems-backend .
   ```

2. **Run the container:**
   ```sh
   docker run -p 8080:8080 ems-backend
   ```

    **OR**
    ```sh
   docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/ems_db -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=root ems-backend-test
    ```
    
     - This command maps port `8080` of the container to port `8080` on your host machine.
     - The environment variables are set to connect to a MySQL database running on your host machine.
     - The service will be available at [http://localhost:8080](http://localhost:8080).

---

## Run with Docker Compose (App + MySQL)

1. **Start services:**
   ```sh
   docker-compose up --build
   ```

    - MySQL will be available on port `3307` of your host.
    - The app will be available at [http://localhost:8080](http://localhost:8080).

2. **Stop and remove containers:**
   ```sh
   docker-compose down
   ```

---

## Notes

- The app connects to MySQL internally at `mysql:3306`.
- If you want to connect to MySQL from your host, use `localhost:3307` with username `root` and password `root`.
- Check logs with:
  ```sh
  docker-compose logs app
  docker-compose logs mysql
  ```

---
```