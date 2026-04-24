# 🛒 Purchasing Microservices Platform (Training Project)

## 📖 Overview

This repository contains a **training project** designed to explore modern backend architecture using **Java + Spring Boot** in a **microservices-based system**.

The application simulates a **purchase workflow**, covering the lifecycle of an order — from creation to inventory validation, payment processing, and notification.

The goal is not to build a production-ready system, but to **learn, experiment, and understand** how distributed systems behave in practice, especially when integrating **event-driven communication** and **AWS cloud services**.

---

## 🎯 Objectives

* Practice **microservices architecture**
* Implement **event-driven communication** using RabbitMQ
* Explore **AWS services** in a realistic scenario
* Understand **data consistency challenges** across services
* Gain hands-on experience with **Docker and Kubernetes (EKS)**

---

## 🧱 Architecture

The system is composed of multiple loosely coupled microservices communicating asynchronously via a message broker.

### Services

* **Order Service**

  * Handles order creation
  * Publishes `OrderCreated` events

* **Inventory Service**

  * Reserves stock
  * Consumes `OrderCreated`
  * Publishes `InventoryReserved` or `InventoryFailed`

* **Payment Service**

  * Processes payments
  * Consumes `InventoryReserved`
  * Publishes `PaymentApproved` or `PaymentFailed`

* **Notification Service**

  * Sends notifications (or logs)
  * Consumes final events

* **Audit Service**

  * Stores domain events for traceability (DynamoDB)

---

## 🔁 Event Flow

```text
Client
  ↓
Order Service
  ↓ (OrderCreated)
RabbitMQ
  ↓
Inventory Service
  ↓ (InventoryReserved)
RabbitMQ
  ↓
Payment Service
  ↓ (PaymentApproved)
RabbitMQ
  ↓
Notification Service
```

---

## 🛠️ Tech Stack

### Backend

* Java 17+
* Spring Boot
* Spring Web
* Spring Data
* Spring AMQP (RabbitMQ)

### Messaging

* RabbitMQ

### Databases

* PostgreSQL (Amazon RDS in cloud)
* DynamoDB (event storage)

### Cloud (AWS)

* EC2 (initial hosting)
* EKS (Kubernetes orchestration)
* Lambda (event-driven processing)
* IAM (authentication & authorization)

### DevOps & Tooling

* Docker & Docker Compose
* Kubernetes (EKS)
* Git

---

## 🐳 Local Development

The project is fully runnable locally using Docker.

### Requirements

* Docker
* Docker Compose
* Java 17+

### Run the environment

```bash
docker-compose up --build
```

This will start:

* RabbitMQ
* PostgreSQL
* DynamoDB (local)
* All microservices

---

## ☁️ Cloud Evolution Strategy

This project is designed to evolve in stages:

1. **Local Environment**

   * Full system running via Docker Compose

2. **EC2 Deployment**

   * Simple cloud hosting with Docker

3. **Managed Databases**

   * Migration to Amazon RDS and DynamoDB

4. **Kubernetes (EKS)**

   * Container orchestration and scaling

5. **Serverless Integration**

   * AWS Lambda for event-driven extensions

---

## 🔐 Security

* AWS IAM roles and policies are used for:

  * Service permissions
  * Resource access control
* No sensitive credentials should be committed

---

## 📂 Project Structure

```text
/services
  /order-service
  /inventory-service
  /payment-service
  /notification-service
  /audit-service

/docker-compose.yml
/k8s
/README.md
```

---

## ⚠️ Disclaimer

This is a **learning-focused project**.

It intentionally prioritizes:

* clarity over completeness
* experimentation over best practices in some areas

Do not use this as-is in production.

---

## 🚀 Future Improvements

* Implement **Saga Pattern** for distributed transactions
* Add **API Gateway**
* Introduce **observability** (tracing, metrics)
* Improve **error handling and retries**
* Add **CI/CD pipeline**

---

## 🤝 Contributions

This is a personal training project, but feel free to fork, explore, and adapt it for your own learning.

---

## 📌 Final Notes

This project is meant to simulate real-world backend challenges in a controlled environment.
Expect rough edges — that’s part of the learning process.

---

