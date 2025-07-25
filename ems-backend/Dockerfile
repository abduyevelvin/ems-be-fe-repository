# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests --batch-mode

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/ems-backend-0.0.1-SNAPSHOT.jar ems-service.jar

# Environment variables for JWT
ENV JWT_SECRET=employee_key
ENV JWT_EXPIRATION=3600000

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ems-service.jar", "--jwt.secret=${JWT_SECRET}", "--jwt.expiration=${JWT_EXPIRATION}"]