# URL Shortening Service

A production-ready URL shortening service built with Spring Boot. This application allows users to generate shortened links, access them, and track basic usage metrics. Designed with extensibility and security in mind.

---

## Features

- Create short URLs that redirect to long URLs
- Custom aliases for URLs (optional)
- Expiration dates and access limits (optional)
- Rate limiting (via Bucket4j)
- User authentication with Spring Security
- RESTful endpoints for frontend or external integration
- MySQL persistence with Spring Data JPA
- Monitoring with Spring Boot Actuator

---

## Tech Stack

- **Backend**: Spring Boot 3.4.4, Spring Web, Spring Security, Spring Data JPA
- **Database**: MySQL
- **View Layer**: Thymeleaf + Spring Security Extras
- **Validation**: Hibernate Validator
- **Rate Limiting**: Bucket4j
- **Dev Tools**: Spring Boot DevTools, Actuator
- **Testing**: JUnit, Spring Security Test
- **Build**: Maven
- **Java Version**: 21
- **Boilerplate Reduction**: Lombok

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8+

### Setup Instructions

1. **Clone the repository**  
   ```bash
   git clone https://github.com/your-username/url-shortener.git
   cd url-shortener
