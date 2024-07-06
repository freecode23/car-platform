# Resque-Car

**Resque-Car** is an IoT-based car platform designed to provide various functionalities such as remote control, sensor data processing, and command communication using Kafka and MQTT.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Introduction

Resque-Car is a comprehensive platform that allows for the remote control of an IoT car and the processing of various sensor data. The platform utilizes Kafka for command and sensor data communication and provides REST APIs for interacting with the car's functionalities.

## Features

- Remote control of the car using REST APIs
- Real-time sensor data processing
- Command and data communication using Kafka and MQTT
- Modular architecture for easy extension and maintenance

## Architecture

The architecture of Resque-Car is composed of the following components:

- **Command Sender**: Sends commands to the car using Kafka.
- **GPS Processing**: Processes GPS data received from the car.
- **Bridge**: Acts as a bridge between different communication protocols.
- **Car Control**: Handles the actual control logic of the car.

## Getting Started

### Prerequisites

- Docker and Docker Compose
