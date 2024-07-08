# Embedded Module

This module is responsible for handling the communication and control logic for the IoT-based car in the Resque-Car project. The embedded module runs on an STM32 microcontroller and interfaces with various peripherals, including a SIM7600 module for MQTT communication and a GPS module.

<img width="523" alt="Screenshot 2024-07-07 at 7 25 16 PM" src="https://github.com/freecode23/resque-car/assets/67333705/7c7c8172-00aa-40e8-b047-8fcd0ad3d6da">

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Future Enhancements](#future-enhancements)

## Introduction

The embedded module is a critical part of the Resque-Car project, designed to enable remote control and real-time sensor data processing. The project aims to test controlling a fleet of cars in disaster-stricken areas where WiFi communication is impossible. This is currently a Work In Progress (WIP).

## Features

- Initialization and configuration of UART and TIM peripherals.
- Communication with AWS IoT MQTT broker using the SIM7600 module.
- Real-time processing of GPS data and sensor data.
- Control logic for the car's movement and data transmission.

## Getting Started

### Prerequisites

- STM32 development environment (STM32CubeIDE recommended).
- Hardware setup: STM32 microcontroller, SIM7600 module, GPS module, and motor driver.

### Building and Flashing

1. Open the project in STM32CubeIDE.
2. Connect the STM32 microcontroller to your computer.
3. Build the project by clicking on the "Build" button.
4. Flash the firmware to the microcontroller by clicking on the "Debug" or "Run" button.

## Project Structure

- **Inc/**: Contains header files for peripheral configuration and function prototypes.
- **Src/**: Contains source files for peripheral initialization, main application logic, and interrupt handlers.
- **Drivers/**: Contains HAL drivers and CMSIS files.
- **startup/**: Contains startup files and linker scripts.

## Technologies Used

- **STM32CubeIDE**: Integrated development environment for STM32 microcontrollers.
- **HAL (Hardware Abstraction Layer)**: Provides a high-level interface to the STM32 peripherals.
- **SIM7600 Module**: Used for MQTT communication with AWS IoT Core.
- **GPS Module**: Provides real-time GPS data.
- **PWM (Pulse Width Modulation)**: Used for motor control.

## Future Enhancements

- Implement temperature sensing to detect living humans.
- Add video/camera processing using Raspberry Pi for object detection.
- Enhance communication protocols for better reliability and performance.

## Communication Overview

The embedded module primarily handles the following communication tasks:

1. **MQTT Communication**: 
   - Initializes connection with AWS IoT MQTT broker.
   - Listens for messages on `topic/cmd`.
   - Publishes sensor data on `topic/sensor`.

2. **GPS Data Processing**:
   - Receives GPS data from the GPS module.
   - Processes and formats the GPS data for transmission.

3. **Control Logic**:
   - Handles control commands received via MQTT.
   - Controls the car's movement using PWM signals.

