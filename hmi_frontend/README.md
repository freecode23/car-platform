# HMI Frontend

This is the frontend component of the Resque-Car project. It provides a web interface for controlling the car and visualizing its status.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Running Locally](#running-locally)
  - [Running in Docker](#running-in-docker)
- [Project Structure](#project-structure)
- [Future Enhancements](#future-enhancements)

## Introduction

The HMI Frontend is a React/Next.js application designed to provide a user-friendly interface for interacting with the Resque-Car. It allows users to send commands to the car and visualize its status.

## Features
- Control the car using W, A, S, D keys or the directional buttons on the interface.
- Display real-time status of the car.

## Getting Started

### Running Locally

To run the application locally, follow these steps:

1. Clone the repository:
    ```bash
    git clone https://github.com/freecode23/resque-car.git
    cd resque-car/hmi_frontend
    ```

2. Install the dependencies:
    ```bash
    npm install
    ```

3. Start the development server:
    ```bash
    npm run dev
    ```

4. Open your browser and navigate to `http://localhost:3005`.

### Running in Docker

To run the application in a Docker container, follow these steps:

1. Build the Docker image:
    ```bash
    docker build -t hmi-frontend .
    ```

2. Run the Docker container:
    ```bash
    docker run -p 3005:3005 hmi-frontend
    ```

3. Open your browser and navigate to `http://localhost:3005`.

## Project Structure

- **public/**: Contains static assets like fonts and images.
- **src/**: Contains the source code for the application.
- **.env**: Environment variables configuration.
- **Dockerfile**: Docker configuration for containerizing the application.
- **package.json**: Project dependencies and scripts.
- **tsconfig.json**: TypeScript configuration.
- **tailwind.config.ts**: Tailwind CSS configuration.


