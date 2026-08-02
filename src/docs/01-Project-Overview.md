# Reverse Proxy & Load Balancer from Scratch using Java 21

> **Project Goal:** Build a production-inspired Reverse Proxy and Load Balancer from scratch using Pure Java 21 without relying on Spring Boot or any networking framework, while understanding the internal working of infrastructure software like NGINX, HAProxy, Envoy, and Traefik.

---

# Table of Contents

1. Project Vision
2. Why This Project?
3. Learning Objectives
4. Project Scope
5. High-Level Architecture
6. Technology Stack
7. Project Structure
8. Development Philosophy
9. Features Roadmap
10. Project Milestones
11. Engineering Principles
12. Expected Outcomes

---

# Project Vision

Most developers know **how to use** reverse proxies like NGINX or HAProxy.

Very few know **how they actually work internally**.

The goal of this project is to understand the architecture behind modern reverse proxies by implementing one from scratch.

Instead of treating infrastructure software as a black box, this project focuses on understanding:

- Socket Programming
- HTTP Protocol
- Reverse Proxy Architecture
- Load Balancing Algorithms
- Health Checks
- Concurrency
- Performance Optimizations
- Production Design Patterns

By the end of this project, the result will not simply be a working reverse proxy, but a deep understanding of distributed systems fundamentals.

---

# Why This Project?

Modern distributed systems rarely expose backend servers directly to clients.

Instead, requests typically follow a path similar to:

```text
Client
   │
   ▼
Load Balancer
   │
   ▼
Reverse Proxy
   │
   ▼
Application Servers
```

Reverse proxies are responsible for:

- Routing requests
- Hiding backend servers
- Improving scalability
- Increasing availability
- Handling failures
- Managing traffic
- Improving performance

Rather than configuring existing tools, this project aims to build these capabilities ourselves.

---

# Learning Objectives

This project is designed to understand the following concepts from first principles.

## Networking

- Socket Programming
- ServerSocket
- TCP Communication
- Client-Server Architecture
- Streams
- Ports

---

## HTTP

- HTTP Request Format
- HTTP Response Format
- HTTP Methods
- HTTP Headers
- Status Codes
- Request Body
- Response Body
- Keep Alive
- Persistent Connections

---

## Java

- Java 21
- Virtual Threads
- HttpClient
- Executors
- Concurrency
- Collections
- Streams
- Builder Pattern

---

## Distributed Systems

- Reverse Proxy
- Load Balancing
- Health Checks
- Failover
- Circuit Breaker
- Service Discovery
- Sticky Sessions
- Rate Limiting

---

## Software Engineering

- SOLID Principles
- Design Patterns
- Layered Architecture
- Loose Coupling
- Separation of Concerns
- Domain Modeling
- Architecture Decision Records (ADR)

---

# Project Scope

The project will gradually evolve through multiple phases.

We will intentionally avoid skipping directly to advanced features.

Every phase builds upon the previous one.

```text
Socket Programming
        │
        ▼
HTTP Parsing
        │
        ▼
Reverse Proxy
        │
        ▼
Multiple Backends
        │
        ▼
Load Balancer
        │
        ▼
Health Checks
        │
        ▼
Monitoring
        │
        ▼
Production Optimizations
```

---

# High-Level Architecture

```text
                    Client
                      │
                      ▼
             Reverse Proxy Server
                      │
        ┌─────────────┴─────────────┐
        ▼                           ▼
Backend Server 1             Backend Server 2
        │                           │
        └─────────────┬─────────────┘
                      ▼
                Backend Server 3
```

The client communicates only with the reverse proxy.

Backend servers remain hidden.

---

# Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Core Language |
| Virtual Threads | Concurrency |
| Socket | Client Communication |
| ServerSocket | Accept Incoming Connections |
| Java HttpClient | Backend Communication |
| Spring Boot | Backend Services (only for testing) |
| IntelliJ IDEA | Development Environment |
| Git | Version Control |

---

# Why Pure Java?

Many frameworks hide important implementation details.

For example:

Spring Boot

↓

Embedded Tomcat

↓

Sockets

↓

TCP

↓

Operating System

Developers often interact only with annotations like:

```java
@RestController

@GetMapping

@PostMapping
```

without understanding the networking stack underneath.

This project intentionally removes those abstractions.

The objective is to understand every layer ourselves.

---

# Why Spring Boot Is Still Used

Although the reverse proxy itself is implemented using Pure Java, backend services are created using Spring Boot.

Reason:

The objective is **not** to build another web framework.

The objective is to build a reverse proxy.

Using Spring Boot backends allows us to focus entirely on proxy behavior while interacting with real HTTP applications.

---

# Project Structure

```text
ReverseProxy/
│
├── docs/
│
├── reverse-proxy/
│
├── backend-service-1/
│
├── backend-service-2/
│
└── backend-service-3/
```

---

## Reverse Proxy Structure

```text
src/
│
├── server/
│
├── backend/
│
├── forwarding/
│
├── http/
│
├── response/
│
├── config/
│
├── metrics/
│
├── logging/
│
├── context/
│
└── util/
```

This structure will evolve throughout the project.

---

# Development Philosophy

The objective is not simply to make the code work.

Each feature should be:

- Understandable
- Extensible
- Testable
- Production-inspired
- Well documented

Every improvement should answer three questions:

1. Why is this needed?
2. How do production systems solve it?
3. Why did we choose this implementation?

---

# Project Phases

## Phase 1

Reverse Proxy

Topics

- Socket Programming
- HTTP Parsing
- Virtual Threads
- Request Forwarding
- Response Writing

---

## Phase 2

Load Balancer

Topics

- Multiple Backend Servers
- Backend Registry
- Round Robin
- Least Connections
- Random Selection

---

## Phase 3

Health Monitoring

Topics

- Health Checks
- Automatic Failover
- Backend Recovery
- Health Scheduler

---

## Phase 4

Performance

Topics

- Connection Pooling
- Keep Alive
- Timeouts
- Streaming
- Compression

---

## Phase 5

Observability

Topics

- Logging
- Metrics
- Request IDs
- Monitoring
- Dashboard Integration

---

## Phase 6

Security

Topics

- HTTPS
- TLS
- Header Validation
- Rate Limiting
- Request Filtering

---

## Phase 7

Advanced Features

Topics

- Sticky Sessions
- Circuit Breaker
- Retry Mechanisms
- Caching
- Service Discovery

---

# Engineering Principles

Throughout the project, the following principles will be followed.

## Single Responsibility Principle

Every class should perform only one responsibility.

---

## Separation of Concerns

Networking, HTTP parsing, forwarding, logging and monitoring should remain independent.

---

## Loose Coupling

Application components should depend on abstractions instead of concrete implementations.

---

## Composition over Inheritance

Classes should collaborate instead of extending one another whenever possible.

---

## Domain Modeling

Internal request and response models should represent the application's own domain instead of exposing Java library classes.

---

## Clean Architecture

The codebase should remain understandable even as new features are added.

---

# Documentation Strategy

Every major concept will have dedicated documentation.

```text
docs/
│
├── 01-Project-Overview.md
├── 02-HTTP-Basics.md
├── 03-Reverse-Proxy-Architecture.md
├── 04-Java-Networking.md
├── 05-HTTP-Protocol.md
├── 06-Concurrency.md
├── 07-Load-Balancing.md
├── 08-Health-Checks.md
├── 09-Performance.md
├── 10-Design-Patterns.md
├── 11-System-Design.md
├── 12-Interview-Questions.md
│
└── decisions/
```

Each document focuses on one major topic.

---

# Architecture Decision Records (ADR)

Every important design decision should be documented.

Example:

```text
ADR-001 Why Pure Java

ADR-002 Why Virtual Threads

ADR-003 Why Layered Architecture

ADR-004 Why HttpClient

ADR-005 Why Domain Models

ADR-006 Why Response Mapper
```

Each ADR answers:

- What problem are we solving?
- What alternatives were considered?
- Why was this approach selected?
- What are the trade-offs?

---

# Expected Outcomes

By the completion of this project, we will have built a production-inspired reverse proxy capable of:

- Accepting HTTP requests
- Parsing HTTP messages
- Forwarding requests to backend services
- Load balancing across multiple servers
- Detecting unhealthy backends
- Automatically failing over
- Logging requests
- Collecting metrics
- Supporting persistent connections
- Implementing common infrastructure patterns

More importantly, we will understand the engineering decisions behind these features instead of relying on existing tools.

---

# Inspiration

This project is conceptually inspired by production infrastructure software such as:

- NGINX
- HAProxy
- Envoy Proxy
- Traefik
- Apache HTTP Server
- Tomcat (HTTP processing concepts)

The objective is **not** to re-create these projects feature-for-feature.

Instead, it is to understand the core architectural ideas that make them reliable, scalable, and maintainable.

---

# Final Goal

The final outcome of this project is not merely a reverse proxy.

It is an educational journey into:

- Network Programming
- HTTP Internals
- Concurrent Programming
- Distributed Systems
- Infrastructure Engineering
- Software Architecture
- Performance Engineering

The emphasis throughout the project is on understanding **why** each design decision is made, not just **how** it is implemented.
