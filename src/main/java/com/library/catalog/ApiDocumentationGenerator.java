package com.library.catalog;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApiDocumentationGenerator {

    public static void main(String[] args) throws Exception {
        String content = generateMarkdown();
        Path docsPath = Path.of("docs", "api.md");
        Files.createDirectories(docsPath.getParent());
        Files.writeString(docsPath, content, StandardCharsets.UTF_8);
        System.out.println("Generated API documentation at " + docsPath.toAbsolutePath());
    }

    public static String generateMarkdown() {
        return "# Library Book Management API\n\n"
                + "## Base URL\n\n"
                + "`http://localhost:8080/api`\n\n"
                + "## Overview\n\n"
                + "Manage books in the library catalog. Successful responses use a JSON envelope with `code`, `message`, `data`, and `timestamp`; errors use `code`, `message`, `timestamp`, and optional field-level `errors`.\n\n"
                + "## Endpoints\n\n"
                + "### GET /api/books\n\n"
                + "- Description: Retrieve all books with pagination support\n"
                + "- Query parameters:\n"
                + "  - `page` (optional integer, default `0`; zero-based and non-negative)\n"
                + "  - `size` (optional integer, default `20`; must be positive)\n"
                + "- Response: `200 OK`; `data` is a Spring page containing `content`, `totalElements`, `totalPages`, `number`, and `size`. Each content item is a book object.\n"
                + "- Errors: `400` for invalid pagination; `500` for unexpected server errors\n"
                + "- Example: `curl 'http://localhost:8080/api/books?page=0&size=20'`\n\n"
                + "### GET /api/books/{isbn}\n\n"
                + "- Description: Retrieve a single book by ISBN\n"
                + "- Path parameter: `isbn` (10 or 13 digits; optional hyphens)\n"
                + "- Response: `200 OK` with a book object in `data`\n"
                + "- Errors: `400` for invalid ISBN; `404` when the book does not exist\n"
                + "- Example: `curl http://localhost:8080/api/books/978-3-16-148410-0`\n\n"
                + "### POST /api/books\n\n"
                + "- Description: Create a new book\n"
                + "- Request body: JSON with `isbn`, `title`, `author`, optional `publisher`, optional `publicationDate` (`YYYY-MM-DD`), `genre`, and optional `quantityAvailable`\n"
                + "- Validation: ISBN is 10 or 13 digits with optional hyphens; title 1-200 characters, author 1-100, genre 1-50; publisher at most 100; publication date cannot be in the future; quantity is a non-negative integer and defaults to `0`\n"
                + "- Response: `201 Created` with the created book object in `data`\n"
                + "- Errors: `400` for validation issues; `409` for duplicate ISBN\n"
                + "- Example: `curl -X POST http://localhost:8080/api/books -H 'Content-Type: application/json' -d '{\"isbn\":\"9783161484100\",\"title\":\"Effective Java\",\"author\":\"Joshua Bloch\",\"genre\":\"Programming\"}'`\n\n"
                + "### PUT /api/books/{isbn}\n\n"
                + "- Description: Partially update an existing book by ISBN; ISBN is immutable and is not an update field\n"
                + "- Path parameter: `isbn` (10 or 13 digits; optional hyphens)\n"
                + "- Request body: JSON containing any of `title`, `author`, `publisher`, `publicationDate`, `genre`, and `quantityAvailable`; omitted fields are unchanged\n"
                + "- Validation: supplied title 1-200 characters, author 1-100, genre 1-50; publisher at most 100; publication date cannot be in the future; quantity is non-negative\n"
                + "- Response: `200 OK` with the updated book object in `data`\n"
                + "- Errors: `400` for invalid ISBN or update data; `404` when the book does not exist\n"
                + "- Example: `curl -X PUT http://localhost:8080/api/books/9783161484100 -H 'Content-Type: application/json' -d '{\"quantityAvailable\":8}'`\n\n"
                + "### DELETE /api/books/{isbn}\n\n"
                + "- Description: Delete a book by ISBN\n"
                + "- Path parameter: `isbn` (10 or 13 digits; optional hyphens)\n"
                + "- Response: `204 No Content` with an empty body\n"
                + "- Errors: `400` for invalid ISBN; `404` when the book does not exist\n"
                + "- Example: `curl -X DELETE http://localhost:8080/api/books/9783161484100`\n\n"
                + "### GET /api/books/search\n\n"
                + "- Description: Search books by case-insensitive partial or full match across title, author, genre, and ISBN\n"
                + "- Query parameters:\n"
                + "  - `title` (required non-blank string; treated as the search term across all searchable fields)\n"
                + "  - `page` (optional integer, default `0`; zero-based and non-negative)\n"
                + "  - `size` (optional integer, default `20`; must be positive)\n"
                + "- Response: `200 OK` with a paginated list of matching book objects in `data`\n"
                + "- Errors: `400` for missing title or invalid pagination\n"
                + "- Example: `curl 'http://localhost:8080/api/books/search?title=java&page=0&size=20'`\n\n"
                + "## Book response object\n\n"
                + "`id` (integer), `isbn` (normalized digits), `title`, `author`, `publisher` (nullable), `publicationDate` (nullable date), `genre`, `quantityAvailable`, `createdAt`, and `updatedAt`.\n\n"
                + "## Error response\n\n"
                + "Errors are returned with the corresponding HTTP status and an envelope containing `code`, `message`, and `timestamp`. Validation errors may include `errors`, a map from field name to a clear message. Statuses: `400` invalid input, `404` book not found, `409` duplicate ISBN, and `500` unexpected server failure. Internal errors do not expose stack traces.\n\n"
                + "## Documentation sync\n\n"
                + "`mvn compile exec:java -Dexec.mainClass=com.library.catalog.ApiDocumentationGenerator` regenerates this document. The Maven build verifies that the generated content matches `docs/api.md` and fails when it drifts.\n";
    }
}
