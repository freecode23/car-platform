# Use an official Maven image to build the project
FROM maven:3.8.1-jdk-11 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the project files to the working directory
COPY pom.xml .
COPY src ./src
COPY target ./target

# Build the project
RUN mvn clean package

# Use an official OpenJDK image to run the application
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy only these files in the final image.
COPY --from=build /app/src ./src
COPY --from=build /app/target ./target


