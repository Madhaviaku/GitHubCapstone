# Library Book Management Catalog

This repository is the GitHub Copilot capstone for an agentic SDLC. Follow the delivery path in `README.md`: requirements, architecture, design review, implementation plan, implementation, code review, verification, and pull request.

## Product

Library administrators manage a book catalog through a Spring Boot REST API.

- CRUD, pagination, and case-insensitive title search
- ISBN uniqueness, field validation, and structured error responses
- `/docs/api.md` must stay in sync with the API. Regenerate it with `ApiDocumentationGenerator` and check it with `ApiDocumentationSyncVerifier`

## Stack

- Java 17, Spring Boot, Spring Data JPA, Bean Validation
- H2 for local development
- JUnit 5 and Mockito
- Maven

## Working rules

- Keep controllers thin. Put business rules in `BookService`.
- Return the standard `ApiResponse` envelope: `code`, `message`, `timestamp`, and optional `errors`.
- Do not change an existing book's ISBN.
- Do not commit secrets, API tokens, or real credentials.
- Match new endpoints and validation rules in `docs/api.md` before considering the change complete.
- Prefer tests for the happy path and for not-found, duplicate ISBN, and missing-field cases.

## Workspace custom agents and skills

The `.github/` customizations are organized into:

- `.github/agents/` — Jira-driven SDLC master and delegated phase agents
- `.github/hooks/` — workspace lifecycle hooks and PowerShell scripts
- `.github/instructions/sdlc.instructions.md` — phase-artifact guidance
- `.github/skills/` — end-to-end SDLC, phase workflows, QA recovery, and conditional browser verification
- `.github/templates/` — Jira assessment and phase artifact templates

Use the SDLC master to assess a Jira issue before entering the phase-gated workflow. Use the phase-specific skills and custom agents for handoff and gate-driven execution. Keep the project aligned to the Library Book Management Catalog requirement baseline in `requirements.md` and do not drift to alternate product assumptions.
