# Library Book Management API

## Base URL

`http://localhost:8080/api`

## Overview

Manage books in the library catalog. Successful responses use a JSON envelope with `code`, `message`, `data`, and `timestamp`; errors use `code`, `message`, `timestamp`, and optional field-level `errors`.

## Endpoints

### GET /api/books

- Description: Retrieve all books with pagination support
- Query parameters:
  - `page` (optional integer, default `0`; zero-based and non-negative)
  - `size` (optional integer, default `20`; must be positive)
- Response: `200 OK`; `data` is a Spring page containing `content`, `totalElements`, `totalPages`, `number`, and `size`. Each content item is a book object.
- Errors: `400` for invalid pagination; `500` for unexpected server errors
- Example: `curl 'http://localhost:8080/api/books?page=0&size=20'`

### GET /api/books/{isbn}

- Description: Retrieve a single book by ISBN
- Path parameter: `isbn` (10 or 13 digits; optional hyphens)
- Response: `200 OK` with a book object in `data`
- Errors: `400` for invalid ISBN; `404` when the book does not exist
- Example: `curl http://localhost:8080/api/books/978-3-16-148410-0`

### POST /api/books

- Description: Create a new book
- Request body: JSON with `isbn`, `title`, `author`, optional `publisher`, optional `publicationDate` (`YYYY-MM-DD`), `genre`, and optional `quantityAvailable`
- Validation: ISBN is 10 or 13 digits with optional hyphens; title 1-200 characters, author 1-100, genre 1-50; publisher at most 100; publication date cannot be in the future; quantity is a non-negative integer and defaults to `0`
- Response: `201 Created` with the created book object in `data`
- Errors: `400` for validation issues; `409` for duplicate ISBN
- Example: `curl -X POST http://localhost:8080/api/books -H 'Content-Type: application/json' -d '{"isbn":"9783161484100","title":"Effective Java","author":"Joshua Bloch","genre":"Programming"}'`

### PUT /api/books/{isbn}

- Description: Partially update an existing book by ISBN; ISBN is immutable and is not an update field
- Path parameter: `isbn` (10 or 13 digits; optional hyphens)
- Request body: JSON containing any of `title`, `author`, `publisher`, `publicationDate`, `genre`, and `quantityAvailable`; omitted fields are unchanged
- Validation: supplied title 1-200 characters, author 1-100, genre 1-50; publisher at most 100; publication date cannot be in the future; quantity is non-negative
- Response: `200 OK` with the updated book object in `data`
- Errors: `400` for invalid ISBN or update data; `404` when the book does not exist
- Example: `curl -X PUT http://localhost:8080/api/books/9783161484100 -H 'Content-Type: application/json' -d '{"quantityAvailable":8}'`

### DELETE /api/books/{isbn}

- Description: Delete a book by ISBN
- Path parameter: `isbn` (10 or 13 digits; optional hyphens)
- Response: `204 No Content` with an empty body
- Errors: `400` for invalid ISBN; `404` when the book does not exist
- Example: `curl -X DELETE http://localhost:8080/api/books/9783161484100`

### GET /api/books/search

- Description: Search books by case-insensitive partial or full match across title, author, genre, and ISBN
- Query parameters:
  - `title` (required non-blank string; treated as the search term across all searchable fields)
  - `page` (optional integer, default `0`; zero-based and non-negative)
  - `size` (optional integer, default `20`; must be positive)
- Response: `200 OK` with a paginated list of matching book objects in `data`
- Errors: `400` for missing title or invalid pagination
- Example: `curl 'http://localhost:8080/api/books/search?title=java&page=0&size=20'`

## Book response object

`id` (integer), `isbn` (normalized digits), `title`, `author`, `publisher` (nullable), `publicationDate` (nullable date), `genre`, `quantityAvailable`, `createdAt`, and `updatedAt`.

## Error response

Errors are returned with the corresponding HTTP status and an envelope containing `code`, `message`, and `timestamp`. Validation errors may include `errors`, a map from field name to a clear message. Statuses: `400` invalid input, `404` book not found, `409` duplicate ISBN, and `500` unexpected server failure. Internal errors do not expose stack traces.

## Documentation sync

`mvn compile exec:java -Dexec.mainClass=com.library.catalog.ApiDocumentationGenerator` regenerates this document. The Maven build verifies that the generated content matches `docs/api.md` and fails when it drifts.
