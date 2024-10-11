# Use OpenJDK image as base
FROM openjdk:17-jdk-alpine


# Create directory for the app
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/*.jar app.jar

# Expose the port that your Spring Boot app runs on (default 8080)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
