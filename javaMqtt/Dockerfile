# Use an official Maven image to build the project
FROM maven:3.8.1-jdk-11 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the project files to the working directory
COPY pom.xml .
COPY src ./src

# Build the project
RUN mvn clean package

# Use an official OpenJDK image to run the application
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/mqtt-client-app-1.0-SNAPSHOT.jar /app/mqtt-client-app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "mqtt-client-app.jar"]
