
# Travel Agency System

## Project Overview
The **Travel Agency System** is a Java-based application developed using **Spring Boot and Microservices Architecture**. It manages travel agency operations, customers, travel packages, bookings, and payments. The project uses Eureka for service discovery, API Gateway for centralized routing, and JWT for authentication.

## Microservices

- **Travel Agency Service:** Manages travel agency-related operations.
- **Booking Service:** Handles travel booking operations.
- **Customer Service:** Manages customer information.
- **Travel Package Service:** Manages travel package details.
- **Payment Service:** Handles payment-related operations.

## Project Architecture

The application follows a microservices architecture in which each service handles a specific business responsibility.

- **Eureka Server:** Registers services and enables service discovery.
- **API Gateway:** Provides a single entry point to access all microservices through a common port.
- **Auth Server:** Supports JWT-based authentication.
- **OpenFeign Client:** Enables communication between microservices.
- **MySQL Database:** Stores application data.

## Key Features

- Modular and maintainable microservices architecture.
- Centralized API routing and service discovery.
- JWT-based authentication.
- Interservice communication using OpenFeign.
- API documentation using Swagger.
- API testing using Postman.
- Unit testing using JUnit and Mockito.

## Tech Stack

- **Language:** Java
- **Backend Framework:** Spring Boot
- **Architecture:** Microservices
- **Service Discovery:** Netflix Eureka Server
- **API Gateway:** Spring Cloud Gateway
- **Interservice Communication:** Spring Cloud OpenFeign
- **Database:** MySQL
- **Authentication:** JWT
- **API Documentation:** Swagger / OpenAPI
- **API Testing:** Postman
- **Unit Testing:** JUnit, Mockito
