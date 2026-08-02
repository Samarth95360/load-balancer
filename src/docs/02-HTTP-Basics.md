# HTTP Basics

> **Goal:** Understand the HTTP protocol from first principles before implementing a Reverse Proxy and Load Balancer.

---

# Table of Contents

1. What is HTTP?
2. Why Do We Need HTTP?
3. Client-Server Architecture
4. HTTP Communication Flow
5. HTTP Request
6. HTTP Response
7. HTTP Methods
8. HTTP Status Codes
9. HTTP Headers
10. HTTP Body
11. URL Structure
12. Query Parameters
13. HTTP Versions
14. Persistent Connections (Keep-Alive)
15. Stateless Nature of HTTP
16. Idempotent vs Safe Methods
17. MIME Types
18. HTTP Request Lifecycle
19. HTTP Response Lifecycle
20. How Our Reverse Proxy Uses HTTP
21. Important Concepts to Remember

---

# What is HTTP?

**HTTP (HyperText Transfer Protocol)** is an **application-layer protocol** used for communication between clients and servers.

Originally designed for transferring HTML pages, HTTP is now used to transfer almost every type of data.

Examples:

- HTML
- JSON
- XML
- Images
- Videos
- PDFs
- ZIP Files

HTTP defines **how data should be requested and transferred**, not **how it travels physically**.

The physical transport is handled by TCP.

---

# Where Does HTTP Fit?

```text
+-----------------------------+
| Application Layer           |
| HTTP                        |
+-----------------------------+
| Transport Layer             |
| TCP                         |
+-----------------------------+
| Internet Layer              |
| IP                          |
+-----------------------------+
| Network Access Layer        |
| Ethernet / WiFi             |
+-----------------------------+
```

HTTP depends on TCP.

TCP does not understand HTTP.

---

# Why Do We Need HTTP?

Imagine there were no communication rules.

A browser sends:

```text
Give me homepage
```

Server responds:

```text
Okay
```

What does "Okay" mean?

Without a standard protocol every application would communicate differently.

HTTP defines a common language understood by every browser and web server.

---

# Client-Server Architecture

HTTP follows the Client-Server model.

```text
        Request
Client ------------> Server

        Response
Client <------------ Server
```

Examples of Clients

- Browser
- Mobile App
- Desktop Application
- CURL
- Postman
- Reverse Proxy

Examples of Servers

- Spring Boot
- Node.js
- Express
- Django
- ASP.NET
- NGINX

---

# HTTP Communication Flow

Suppose a user opens

```
http://localhost:8080/api/users
```

The flow becomes

```text
Browser
    │
TCP Connection
    │
HTTP Request
    │
Server
    │
HTTP Response
    │
Browser
```

Our reverse proxy sits between them.

```text
Browser
    │
HTTP Request
    │
Reverse Proxy
    │
HTTP Request
    │
Backend Server
    │
HTTP Response
    │
Reverse Proxy
    │
HTTP Response
    │
Browser
```

---

# HTTP Request

Every request has four main parts.

```text
Request Line

Headers

Blank Line

Body
```

Example

```http
GET /api/users HTTP/1.1
Host: localhost:8080
Accept: application/json
User-Agent: Mozilla/5.0

```

Notice the blank line.

It marks the end of headers.

---

# Request Line

The request line contains

```text
METHOD PATH VERSION
```

Example

```http
GET /users HTTP/1.1
```

Components

Method

```
GET
```

Path

```
/users
```

Version

```
HTTP/1.1
```

Our `HttpRequestParser` parses exactly this line first.

---

# HTTP Methods

HTTP methods define what operation should be performed.

## GET

Retrieve data.

Example

```http
GET /users
```

Characteristics

- Safe
- Idempotent
- No request body (typically)

---

## POST

Create new resources.

```http
POST /users
```

Usually contains a request body.

---

## PUT

Replace an existing resource.

```http
PUT /users/1
```

Usually idempotent.

---

## PATCH

Update part of a resource.

```http
PATCH /users/1
```

Partial update.

---

## DELETE

Remove a resource.

```http
DELETE /users/1
```

Usually idempotent.

---

## HEAD

Same as GET but returns headers only.

No body.

Useful for

- File size
- Last modified date
- Health checks

---

## OPTIONS

Returns supported HTTP methods.

Often used in CORS.

---

# Safe vs Unsafe Methods

Safe methods never modify server state.

Safe

- GET
- HEAD
- OPTIONS

Unsafe

- POST
- PUT
- PATCH
- DELETE

---

# Idempotent Methods

Calling an idempotent request multiple times produces the same result.

Example

DELETE user 10

First request

User deleted.

Second request

User is already deleted.

Result remains the same.

Idempotent

- GET
- PUT
- DELETE
- HEAD
- OPTIONS

Not Idempotent

- POST

---

# HTTP Response

Every response has four parts.

```text
Status Line

Headers

Blank Line

Body
```

Example

```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 18

{"name":"John"}
```

---

# Status Line

Structure

```text
VERSION STATUS_CODE REASON
```

Example

```http
HTTP/1.1 404 Not Found
```

Our `HttpResponseWriter` creates this line.

---

# HTTP Status Codes

## 1xx

Informational

Example

```
100 Continue
```

---

## 2xx

Success

Examples

```
200 OK

201 Created

204 No Content
```

---

## 3xx

Redirection

Examples

```
301 Moved Permanently

302 Found
```

---

## 4xx

Client Errors

Examples

```
400 Bad Request

401 Unauthorized

403 Forbidden

404 Not Found

405 Method Not Allowed
```

---

## 5xx

Server Errors

Examples

```
500 Internal Server Error

502 Bad Gateway

503 Service Unavailable

504 Gateway Timeout
```

---

# Why Reverse Proxies Need Status Codes

Sometimes the backend never responds.

Instead of crashing, the proxy generates

```http
HTTP/1.1 502 Bad Gateway
```

or

```http
HTTP/1.1 504 Gateway Timeout
```

This is why our project contains a `HttpStatus` enum.

---

# HTTP Headers

Headers provide additional information.

Example

```http
Content-Type: application/json
Content-Length: 120
Authorization: Bearer token
Accept: application/json
```

Headers are

```text
Key : Value
```

---

# Common Request Headers

| Header | Purpose |
|---------|----------|
| Host | Target host |
| User-Agent | Client information |
| Accept | Accepted response type |
| Authorization | Authentication |
| Cookie | Session information |
| Content-Type | Body format |
| Content-Length | Body size |

---

# Common Response Headers

| Header | Purpose |
|---------|----------|
| Content-Type | Response format |
| Content-Length | Response size |
| Set-Cookie | Creates cookies |
| Cache-Control | Cache behavior |
| Location | Redirect destination |
| Server | Server information |

---

# Hop-by-Hop Headers

Some headers must **never** be forwarded by a reverse proxy.

Examples

- Connection
- Keep-Alive
- Transfer-Encoding
- Upgrade
- Proxy-Authenticate
- Proxy-Authorization
- Trailer

These belong only to one TCP connection.

We'll handle them in a later phase.

---

# End-to-End Headers

These headers travel all the way from client to backend.

Examples

- Authorization
- Accept
- Content-Type
- Cookie
- Cache-Control

These are forwarded by the proxy.

---

# HTTP Body

The body contains actual data.

Examples

JSON

```json
{
    "name":"Samarth"
}
```

Image

```
profile.png
```

PDF

```
invoice.pdf
```

Video

```
movie.mp4
```

HTTP does not care about the content.

It simply transfers bytes.

---

# Why Our Project Uses byte[]

Instead of

```java
String body;
```

we use

```java
byte[] body;
```

Reason

HTTP transfers bytes.

Not every response is text.

Advantages

Supports

- JSON
- Images
- Videos
- PDFs
- ZIP Files
- Audio

without modification.

---

# URL Structure

Example

```
https://example.com:8080/api/users?id=10&page=2
```

Breakdown

Protocol

```
https
```

Host

```
example.com
```

Port

```
8080
```

Path

```
/api/users
```

Query

```
id=10&page=2
```

---

# Query Parameters

Everything after

```
?
```

Example

```
/users?id=10&page=2
```

Our parser will support these in a later phase.

---

# MIME Types

Content-Type tells clients how to interpret the body.

Examples

```text
application/json

text/html

text/plain

image/png

image/jpeg

application/pdf

application/xml

application/zip
```

The proxy simply forwards this header.

---

# HTTP Versions

## HTTP/1.0

One TCP connection per request.

Slow.

---

## HTTP/1.1

Persistent connections.

Default version used today.

Our proxy currently supports HTTP/1.1.

---

## HTTP/2

Multiplexing.

Binary protocol.

Header compression.

Supported internally by Java HttpClient.

---

## HTTP/3

Runs over QUIC instead of TCP.

Much faster.

Out of scope for this project.

---

# Persistent Connections (Keep-Alive)

Without Keep-Alive

```text
Request

↓

Open TCP

↓

Response

↓

Close TCP
```

Every request creates a new connection.

---

With Keep-Alive

```text
Open TCP

↓

Request

↓

Response

↓

Request

↓

Response

↓

Close TCP
```

Far more efficient.

We'll implement this later.

---

# Stateless Nature of HTTP

HTTP is stateless.

Example

Request 1

```
GET /login
```

Request 2

```
GET /profile
```

The server does not automatically remember the previous request.

State is maintained using

- Cookies
- Sessions
- JWT
- Tokens

---

# HTTP Request Lifecycle

```text
Client

↓

DNS Lookup

↓

TCP Connection

↓

HTTP Request

↓

Reverse Proxy

↓

Backend Server
```

---

# HTTP Response Lifecycle

```text
Backend

↓

HTTP Response

↓

Reverse Proxy

↓

Client
```

---

# How Our Reverse Proxy Uses HTTP

Request

```text
Raw HTTP Request

↓

HttpRequestParser

↓

HttpRequestData

↓

RequestForwarder
```

Response

```text
HttpResponse<String>

↓

HttpResponseMapper

↓

HttpResponseData

↓

HttpResponseWriter

↓

Raw HTTP Response
```

This separation keeps the architecture clean and loosely coupled.

---

# Important Concepts to Remember

## HTTP is an Application Layer Protocol

It defines communication rules.

TCP handles transport.

---

## HTTP is Stateless

Each request is independent.

---

## HTTP Transfers Bytes

Not Strings.

Always think in terms of bytes.

---

## Headers and Body are Separate

The blank line separates them.

---

## Request and Response Have Similar Structure

Request

```text
Request Line

Headers

Blank Line

Body
```

Response

```text
Status Line

Headers

Blank Line

Body
```

---

## Reverse Proxy Responsibilities

- Receive requests
- Parse HTTP
- Select backend
- Forward request
- Receive response
- Return response

The proxy should remain transparent whenever possible.

---

# Summary

By understanding HTTP at the protocol level, we can appreciate why our reverse proxy is structured the way it is.

Every component we build—request parser, response writer, status model, header handling, and forwarding logic—is based directly on the HTTP specification rather than framework abstractions.

A strong understanding of HTTP is the foundation for building reliable infrastructure software such as reverse proxies, load balancers, API gateways, and web servers.