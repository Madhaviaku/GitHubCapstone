# Changelog

## Unreleased

- Enhanced `GET /api/books/search` to return paginated, partial, case-insensitive matches across title, author, genre, and ISBN (hyphens are removed from the ISBN search term), while retaining the required `title` query parameter and existing response contract.
- Added gated, Jira-aware SDLC workspace guidance, phase agents and skills, templates, and lifecycle hooks aligned with this Java/Spring Boot catalog repository.
- Updated catalog request validation and defaults, prevented ISBN changes during updates, and expanded structured handling of malformed requests.
