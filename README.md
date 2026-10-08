# URL Shortener Service

A Spring Boot application that turns long URLs into short, shareable links and redirects visitors to the original URL. It has both a **REST API** and a simple **web interface** (Thymeleaf), and tracks how many times each short link is opened.

## Features

- Shorten any valid URL through the web form or the REST API
- Short codes generated with **Base62 encoding** of the database ID, so every code is unique with no collision checks
- Redirect (`302`) from the short link to the original URL
- **Click count** tracking for every short link
- Input validation (`@NotEmpty`, `@URL`) with clear error messages
- Global exception handling that returns a consistent `404` JSON response for unknown short codes
- Health check endpoint (`/ping`)

## Tech Stack

- Java 21, Spring Boot 4.1
- Spring MVC, Spring Data JPA, Hibernate
- Thymeleaf (web UI)
- H2 database (file based)
- Bean Validation, Lombok, Maven

## How It Works

1. The original URL is saved, and the database generates an auto-increment ID.
2. The ID is converted to a Base62 string (`0-9`, `a-z`, `A-Z`). For example, ID `125` becomes `21`.
3. The short code is saved against the record. Because IDs are unique, so are the codes.
4. When someone opens `/{shortCode}`, the app looks up the record, increments `clickCount`, and redirects to the original URL.

## API

| Method | Endpoint | Description |
|---|---|---|
| GET | `/` | Web interface |
| POST | `/shorten-web` | Form submit from the web UI |
| POST | `/api/v1/url/shorten` | Create a short URL |
| GET | `/{shortCode}` | Redirect to the original URL (`302`) |
| GET | `/ping` | Health check |

**Create a short URL**

```bash
curl -X POST http://localhost:8080/api/v1/url/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://www.example.com/some/very/long/path"}'
```

Response (`201 Created`):

```json
{ "shortUrl": "http://localhost:8080/1" }
```

**Unknown short code** (`404 Not Found`):

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Short code not found: xyz",
  "timestamp": "2026-10-09T10:15:30"
}
```

## Running Locally

**Prerequisites:** Java 21 and Maven

```bash
git clone https://github.com/VaibhaviJantralwala/url-shortener-springboot.git
cd url-shortener-springboot
./mvnw spring-boot:run
```

Open `http://localhost:8080` in your browser.

**Configuration** (`src/main/resources/application.properties`):

| Property | Purpose | Default |
|---|---|---|
| `app.base-url` | Base used when building the short link | `http://localhost:8080` |
| `spring.datasource.url` | H2 file database location | `jdbc:h2:file:./data/urlshortner` |

The H2 console is enabled for local development only. Turn it off before deploying (`spring.h2.console.enabled=false`).

## Project Structure

```
src/main/java/com/spring
├── controller/   # REST and page controllers
├── service/      # shortening logic (Base62 encoding, click counting)
├── dao/          # Spring Data JPA repository
├── model/        # UrlMapping entity
├── dto/          # request and response objects
└── exception/    # custom exception and global handler
```

## Known Limitations and Next Steps

- Codes are sequential, so they are guessable. Options include a random salt or a different encoding.
- No link expiry, custom aliases or user accounts yet
- H2 is used for simplicity. A production setup would use MySQL or PostgreSQL.
- Planned: unit tests, Docker support, an analytics view for click counts

## Author

**Vaibhavi Jantralwala**
Java Backend Developer | [LinkedIn](https://linkedin.com/in/vaibhavijantralwala) | [GitHub](https://github.com/VaibhaviJantralwala)
