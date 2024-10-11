# First stage: Build the Spring Boot app using Gradle
FROM gradle:7.6.0-jdk17-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle wrapper and build files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Give execute permissions to the Gradle wrapper
RUN chmod +x gradlew

# Copy the application source code
COPY src /app/src

# Build the Spring Boot application
RUN ./gradlew build --no-daemon

# Second stage: Create the final image using OpenJDK
FROM openjdk:17-jdk-alpine

# Set the working directory for the app
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port that your Spring Boot app uses (default 8080)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
