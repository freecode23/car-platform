# Resque-Car

**Resque-Car** is an IoT-based car platform designed to provide various functionalities such as remote control, sensor data processing, and command communication using Kafka and MQTT.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Architecture](#architecture)
- [Directories](#directories)
- [Features in Development](#features-in-development)

## Introduction

Resque-Car is a comprehensive platform that allows for the remote control of an IoT car and the processing of various sensor data. The platform utilizes Kafka for command and sensor data communication and provides REST APIs for interacting with the car's functionalities. This rescue car project aims to test controlling a fleet of cars in disaster-stricken areas where WiFi communication is impossible. This is currently a work in progress (WIP).

## Features

- Remote control of the car using REST APIs
- Real-time sensor data processing
- Command and data communication using Kafka and MQTT
- Modular architecture for easy extension and maintenance

## Architecture

The architecture of Resque-Car is composed of the following components:

<img width="702" alt="Screenshot 2024-07-07 at 5 18 20 PM" src="https://github.com/freecode23/resque-car/assets/67333705/1b9a1470-2114-46e8-b9e2-accc448049d3">

Each rectangular shape in the diagram represents a microservice running in a Docker container.

- **Kafka**: Acts as the central message broker for the system, handling communication between various components.
  
- **Command Sender**: Sends commands to the car using Kafka. It receives API requests from the frontend and produces Kafka messages that are consumed by the car for execution.
  
- **GPS Processing**: Processes GPS data received from the car. It consumes GPS data messages from Kafka, processes the data, and stores it in TimescaleDB for later use.
  
- **Car Status**: Another Kafka consumer module that processes car status data and stores it in TimescaleDB.
  
- **Bridge**: Acts as a bridge between different communication protocols. It consumes messages from Kafka and publishes them to the MQTT broker, and vice versa. This allows seamless integration between Kafka and MQTT protocols.
  
- **MQTT Broker (AWS IoT Core)**: Facilitates communication with the IoT car over MQTT protocol. It receives commands from the bridge and sends sensor data to the bridge.
  
- **TimescaleDB**: Stores all the GPS and status data for the car. This time-series database is optimized for handling large volumes of timestamped data.
  
- **Grafana**: Provides a visualization layer for the data stored in TimescaleDB. Users can create dashboards to monitor car data in real time.
  
- **HMI frontend**: The frontend application for interacting with the car. It provides a web interface for sending commands and querying the car's status and GPS data.

## Directories

### data_platform/

This directory contains the backend services and data processing modules. It includes the Kafka consumers, producers, and database interaction logic. Each submodule has specific responsibilities, such as handling GPS data or command messages.

### embedded/

This directory includes the firmware and embedded code running on the IoT car. It handles the direct interaction with the car's hardware components, such as sensors and actuators. This code is responsible for sending sensor data and executing received commands.

### hmi_frontend/

This directory holds the frontend code for the Human-Machine Interface (HMI). It includes the React/Next.js application that provides the user interface for controlling the car and visualizing sensor data. Users can interact with the car and view real-time updates through this web application.

Please refer to each directory for more detailed information on each module.

## Features in Development

- Real-time map of the car
- Temperature sensing to detect living humans
- Video/camera processing (detection) using Raspberry Pi
