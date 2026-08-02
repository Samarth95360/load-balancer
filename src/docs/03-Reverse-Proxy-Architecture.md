# Phase 1 - Building a Reverse Proxy from Scratch (Java 21)

---

# Goal

The objective of Phase 1 is to build a working HTTP Reverse Proxy using **Pure Java 21**, without using Spring Boot, Maven frameworks, or third-party networking libraries.

Instead of relying on existing reverse proxies like NGINX or HAProxy, we are implementing the core concepts ourselves to understand how they work internally.

---

# What is a Reverse Proxy?

A reverse proxy is a server that sits between the client and backend servers.

Instead of clients communicating directly with backend services, every request first reaches the reverse proxy.

```
Browser
    │
    ▼
Reverse Proxy
    │
    ▼
Backend Server
```

The client never knows which backend actually processed the request.

---

# Difference Between Forward Proxy and Reverse Proxy

## Forward Proxy

```
Client
   │
Forward Proxy
   │
Internet
```

Examples

- Company proxy
- VPN
- Anonymous browsing

The proxy represents the client.

---

## Reverse Proxy

```
Internet
    │
Reverse Proxy
    │
Backend Servers
```

Examples

- NGINX
- HAProxy
- Envoy

The proxy represents the backend servers.

---

# Technologies Used

- Java 21
- Virtual Threads (Project Loom)
- Socket Programming
- ServerSocket
- Java HttpClient
- Spring Boot (only as backend service)

---

# Project Structure

```
src/
│
├── server/
│      ProxyServer.java
│      ProxyHandler.java
│
├── backend/
│      BackendServer.java
│
├── forwarding/
│      RequestForwarder.java
│
├── http/
│      HttpRequestData.java
│      HttpRequestParser.java
│      HttpResponseData.java
│      HttpResponseMapper.java
│      HttpStatus.java
│
├── response/
│      HttpResponseWriter.java
```

---

# High Level Flow

```
Browser
    │
GET /api/hello
    │
    ▼
ProxyServer
    │
accept()
    │
    ▼
Virtual Thread
    │
    ▼
ProxyHandler
    │
Parse Request
    │
    ▼
HttpRequestData
    │
Select Backend
    │
    ▼
RequestForwarder
    │
Java HttpClient
    │
    ▼
Spring Boot Backend
    │
HttpResponse<String>
    │
    ▼
HttpResponseMapper
    │
HttpResponseData
    │
    ▼
HttpResponseWriter
    │
Raw HTTP Response
    │
    ▼
Browser
```

---

# Why Pure Java?

Purpose:

- Learn networking fundamentals
- Understand HTTP protocol
- Understand reverse proxy internals
- Learn system design
- Build production-level architecture

Spring Boot hides these details.

---

# Important Java Networking Classes

## ServerSocket

Responsible for listening for incoming client connections.

```
Browser

↓

ServerSocket.accept()

↓

Socket
```

Example

```java
ServerSocket serverSocket =
        new ServerSocket(8080);
```

Think of it as the **main entrance** of the application.

---

## Socket

Represents one connected client.

```
Client

↓

Socket
```

Each connected browser gets its own Socket.

Example

```java
Socket clientSocket =
        serverSocket.accept();
```

---

# Difference

ServerSocket

- Listens for new connections
- Only one instance

Socket

- Represents one connected client
- One socket per client

---

# Why Virtual Threads?

Without Virtual Threads

```
Client A

↓

Processing

↓

Client B waits

↓

Client C waits
```

Only one request is processed at a time.

---

With Virtual Threads

```
Client A

↓

Virtual Thread 1

Client B

↓

Virtual Thread 2

Client C

↓

Virtual Thread 3
```

Each request executes independently.

Implementation

```java
while (true) {

    Socket socket = serverSocket.accept();

    Thread.startVirtualThread(() -> {
        handleClient(socket);
    });

}
```

Advantages

- Lightweight
- Thousands of concurrent clients
- Simple programming model
- Perfect for I/O-heavy applications

---

# Responsibilities of Each Class

## ProxyServer

Responsibilities

- Opens ServerSocket
- Accepts connections
- Creates Virtual Threads
- Delegates to ProxyHandler

Never parses HTTP.

---

## ProxyHandler

Responsibilities

- Reads socket
- Parses request
- Chooses backend
- Calls RequestForwarder
- Maps response
- Sends response

Acts as the request orchestrator.

---

## HttpRequestParser

Responsibilities

- Reads raw HTTP request
- Parses

    - Method
    - Path
    - Version
    - Headers

Produces

```
HttpRequestData
```

---

## HttpRequestData

Represents one HTTP request inside our application.

Acts as our domain model.

Later it will include

- Body
- Query Parameters
- Cookies
- Remote Address

---

## BackendServer

Represents one backend application.

Currently stores

- Host
- Port

Later it will store

- Health
- Weight
- Active Connections
- Latency
- Failure Count

---

## RequestForwarder

Responsible for communicating with backend servers.

Uses

```java
HttpClient
```

Responsibilities

- Build backend URL
- Create HttpRequest
- Send request
- Receive response

Returns

```
HttpResponse<String>
```

---

# Why HttpClient?

Instead of manually opening another socket, Java HttpClient handles

- HTTP formatting
- Headers
- TCP connections
- Connection reuse
- HTTP/2 support

Our focus is Reverse Proxy architecture rather than low-level HTTP serialization.

---

## HttpResponseMapper

Purpose

Convert

```
HttpResponse<String>
```

into

```
HttpResponseData
```

Why?

To decouple our application from Java HttpClient.

Only one class knows Java's HttpResponse.

---

## HttpResponseData

Our internal HTTP Response model.

Contains

- HttpStatus
- Headers
- Body

Uses

```java
byte[]
```

instead of

```java
String
```

because HTTP transfers bytes, not text.

Supports

- JSON
- Images
- Videos
- ZIP
- PDF

without modification.

---

## HttpResponseWriter

Responsible for writing the HTTP response back to the browser.

Steps

1. Write Status Line

2. Write Headers

3. Blank Line

4. Raw Body Bytes

```
HTTP/1.1 200 OK

Content-Type: text/plain

Content-Length: 5

Hello
```

Headers and Body are written separately.

---

# Why Separate Header and Body?

Incorrect

```
StringBuilder

↓

Everything
```

Correct

```
Headers

↓

Socket

↓

Body Bytes

↓

Socket
```

Binary files should never be converted into Strings.

---

# HttpStatus

Instead of using magic numbers

```
404

502

200
```

we use

```java
HttpStatus.NOT_FOUND

HttpStatus.BAD_GATEWAY

HttpStatus.OK
```

Benefits

- Readable
- Type Safe
- No duplicated reason phrases

---

# Architecture Principles Followed

- Single Responsibility Principle
- Separation of Concerns
- Loose Coupling
- Domain Models
- Layered Architecture
- Composition over Inheritance

---

# Current Request Lifecycle

```
Client

↓

Socket

↓

ProxyHandler

↓

HttpRequestParser

↓

HttpRequestData

↓

BackendServer

↓

RequestForwarder

↓

Java HttpClient

↓

Spring Boot Backend

↓

HttpResponse<String>

↓

HttpResponseMapper

↓

HttpResponseData

↓

HttpResponseWriter

↓

Client
```

---

# Improvements Completed

✅ Working Reverse Proxy

✅ Virtual Threads

✅ BackendServer abstraction

✅ Request parser

✅ Response model

✅ Response mapper

✅ HttpStatus abstraction

✅ Response writer

---

# Upcoming Improvements

- Request Body Parsing
- POST / PUT / PATCH support
- Header forwarding
- Timeout handling
- Error handling
- Configuration system
- Logging
- Metrics
- Keep-Alive
- Connection Pooling

After completing these, we will begin implementing the Load Balancer.

---

# Final Architecture (Current)

```
                     Browser
                         │
                         ▼
                 ProxyServer
                         │
                  ServerSocket
                         │
                    accept()
                         │
                  Virtual Thread
                         │
                         ▼
                  ProxyHandler
             ┌───────────┴───────────┐
             ▼                       ▼
     HttpRequestParser       RequestForwarder
             │                       │
             ▼                       ▼
     HttpRequestData        HttpClient
                                     │
                                     ▼
                            Spring Boot Backend
                                     │
                                     ▼
                         HttpResponse<String>
                                     │
                                     ▼
                          HttpResponseMapper
                                     │
                                     ▼
                           HttpResponseData
                                     │
                                     ▼
                          HttpResponseWriter
                                     │
                                     ▼
                                  Browser
```

---

# Key Learnings

- Difference between ServerSocket and Socket
- Reverse Proxy architecture
- Java Socket Programming
- Virtual Threads
- HTTP Request lifecycle
- HTTP Response lifecycle
- Layered Architecture
- Separation of Concerns
- Domain Modeling
- Loose Coupling
- Why proxies transfer bytes instead of Strings
- Why infrastructure software uses internal request/response models
- Why HttpClient is hidden behind an abstraction

# Why Layered Architecture?

Instead of putting all networking logic inside a single class, the application is divided into layers.

Browser
│
▼
ProxyHandler
│
▼
HttpRequestParser
│
▼
RequestForwarder
│
▼
HttpResponseMapper
│
▼
HttpResponseWriter

Each layer has only one responsibility.

Benefits

- Easier to test
- Easier to debug
- Easier to extend
- Better maintainability
- Follows Single Responsibility Principle

If one layer changes, the remaining layers remain unaffected.

# Request Life Cycle

Browser

↓

Socket

↓

ProxyHandler

↓

HttpRequestParser

↓

HttpRequestData

↓

RequestForwarder

↓

Backend

The request always flows toward the backend.


# Response Life Cycle

Backend

↓

HttpResponse<String>

↓

HttpResponseMapper

↓

HttpResponseData

↓

HttpResponseWriter

↓

Browser

The response always flows back toward the client.

Understanding these two independent flows is one of the most important concepts in Reverse Proxy design.

# Why Create Our Own Request and Response Objects?

Java already provides

HttpRequest

HttpResponse

So why create

HttpRequestData

HttpResponseData

Reason

Our application should not depend on Java's implementation.

Instead

Java HttpResponse

↓

HttpResponseMapper

↓

HttpResponseData

Now the rest of the application only understands our own model.

Benefits

- Loose coupling
- Easier testing
- Easy to replace HttpClient
- Easier future extensions

This technique is commonly known as an Anti-Corruption Layer in Domain Driven Design.

# Why Not Spring Boot?

Spring Boot hides networking details.

Example

@RestController

@GetMapping

↓

Tomcat

↓

Sockets

↓

HTTP

All low-level networking is abstracted.

Our objective is to understand

- Socket Programming
- HTTP Protocol
- Threading
- Reverse Proxy Architecture

Once these concepts are understood, frameworks become much easier to use.

# Thread Per Request Model

Each incoming client connection receives one Virtual Thread.

Browser A

↓

Virtual Thread 1

Browser B

↓

Virtual Thread 2

Browser C

↓

Virtual Thread 3

Each request is processed independently.

Advantages

- Simple programming model
- High scalability
- Excellent for blocking I/O

Java 21 Virtual Threads make this approach practical for thousands of concurrent requests.

# Separation of Transport Layer and Business Layer

Networking Layer

- Socket
- ServerSocket
- Streams

HTTP Layer

- Parser
- Request
- Response

Routing Layer

- Backend Selection

Forwarding Layer

- HttpClient

Serialization Layer

- Response Writer

Each layer performs one job only.

This separation allows each component to evolve independently.

# Why Response Body Uses byte[]

HTTP transfers bytes.

Examples

JSON

↓

byte[]

Image

↓

byte[]

Video

↓

byte[]

PDF

↓

byte[]

The proxy should never assume the content is text.

Using byte[] makes the proxy content-agnostic.

This allows forwarding of any file type without modification.

# Why Use Java HttpClient?

Our proxy receives requests using raw sockets.

However, when communicating with backend servers, Java HttpClient is used.

Reasons

- Production-quality implementation
- HTTP/1.1 support
- HTTP/2 support
- Automatic connection pooling
- Automatic header formatting
- Timeout support

This allows us to focus on Reverse Proxy architecture rather than HTTP serialization.

In later phases, this abstraction could even be replaced.

# Why HttpResponseMapper Exists

Only one class should understand Java HttpClient.

Without Mapper

Entire Application

↓

HttpResponse<String>

With Mapper

HttpResponse<String>

↓

HttpResponseMapper

↓

HttpResponseData

This isolates Java-specific implementation details from the rest of the application.

This design is called an Adapter Pattern.

# SOLID Principles

Single Responsibility Principle

Every class performs one responsibility.

Examples

ProxyServer

↓

Accept Connections

ProxyHandler

↓

Orchestrate Request

RequestForwarder

↓

Communicate with Backend

HttpResponseWriter

↓

Write HTTP Response

Open Closed Principle

Future load balancing algorithms can be added without modifying existing forwarding logic.

Dependency Inversion

Application depends on abstractions like HttpRequestData and HttpResponseData instead of Java HttpClient directly.

# Design Patterns Used

Adapter Pattern

HttpResponseMapper

Converts Java HttpResponse into our own model.

Data Transfer Object (DTO)

HttpRequestData

HttpResponseData

These objects carry data between application layers.

Composition

ProxyHandler composes Parser, Forwarder and Writer instead of extending them.

Layered Architecture

Entire project is divided into networking, parsing, forwarding and response layers.

# Current Limitations

Current implementation supports

✓ GET Requests

✓ One Backend

✓ HTTP/1.1

Current implementation does not yet support

✗ POST Body

✗ Keep Alive

✗ Streaming

✗ Multiple Backends

✗ Health Checks

✗ HTTPS

✗ Load Balancing

These will be implemented in upcoming phases.

# Upcoming Phases

Phase 2

- Multiple Backend Servers
- Round Robin
- Least Connections

Phase 3

- Health Checks
- Failover

Phase 4

- Metrics
- Logging

Phase 5

- Rate Limiting

Phase 6

- HTTPS

Phase 7

- HTTP/2

Phase 8

- WebSocket Proxying