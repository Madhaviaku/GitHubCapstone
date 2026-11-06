# Code Review

## Review Checklist

### Correctness
Question: Does each component behave as specified in requirements.md?

Status: Mostly Pass with one design gap

- The controller and service support the required CRUD flows for books, title search, validation, and Not Found handling.
- The API contract aligns with the requirements for create, read, update, and delete operations.
- The library catalog logic normalizes ISBN values, prevents duplicate ISBNs, and enforces required field validation.
- Observation: the update request model supports an optional `isbn` field, which allows changing the identifier during update. This conflicts with the requirement that ISBN cannot be changed after creation. This should be reviewed and either removed from the update payload or explicitly restricted by business rule.

### Security
Question: Are secrets excluded from output? Is user input validated?

Status: Pass

- No hardcoded secrets or credentials were found in the application source.
- `application.properties` contains an empty password placeholder rather than a real secret.
- User input is validated at the request boundary using Jakarta validation annotations (`@NotBlank`, `@Pattern`, `@Size`, `@Min`, `@PastOrPresent`).
- The service layer also performs defensive normalization and validation for ISBN values and required book fields.

### Error Handling
Question: Are all API failures, missing files, and empty repos handled gracefully?

Status: Pass

- `GlobalExceptionHandler` handles `BookNotFoundException`, `DuplicateIsbnException`, validation failures, illegal arguments, and generic server errors.
- API responses return consistent error payloads with status codes such as 400, 404, 409, and 500.
- Missing record flow is handled by returning a clear 404 response with the ISBN in the message.
- The app does not read from a filesystem or external repo in this scope, so repository/file errors are not relevant to this implementation.

### Test Coverage
Question: Do tests cover the happy path AND the 'Not Found' / missing-field edge cases?

Status: Good, with some gaps

- Covered: successful create flow, duplicate ISBN rejection, missing book lookup, invalid quantity, and required-field validation.
- Covered in integration tests: create success, Not Found for missing book, and validation failure for required fields.
- Remaining gaps: search-by-title validation, blank search input, and duplicate update scenario are not explicitly covered.
- Recommendation: add a few focused tests for invalid search input and update path edge cases to strengthen coverage.

### Code Clarity
Question: Are function names self-explanatory? Is logic easy to follow without comments?

Status: Pass

- Method names such as `getBooks`, `createBook`, `updateBook`, `searchBooks`, and `normalizeIsbn` are clear and descriptive.
- The classes are organized logically into controller, service, repository, DTO, and exception layers.
- The validation and error-handling flow is easy to follow without additional comments.

### DRY Principle
Question: Is there duplicated logic that Copilot can refactor into a shared function?

Status: Mostly Pass

- There is little obvious duplication across the service methods.
- Validation rules are checked directly in `createBook` and `updateBook`, which is acceptable for a small codebase.
- A small shared validation helper could reduce repeated null/blank checks if the project grows, but it is not required for correctness right now.

### Dependency Safety
Question: Does Copilot flag any known vulnerable package versions?

Status: No immediate issue identified

- The project uses Spring Boot 3.3.5 and related dependencies from the managed Spring BOM, which is a current stable version.
- No obvious vulnerable or unsafe package versions were detected during the code review.
- A formal dependency scan (for example, Maven OWASP or dependency-audit tooling) would be the next step for a production-grade security review.

## Overall Review Result
Recommendation: Approved with minor follow-up

- The implementation satisfies the core functional requirements and validation expectations.
- The main follow-up item is to tighten the update behavior so that ISBN changes are not allowed, matching the requirements specification.
- The current test suite demonstrates strong baseline coverage for the implemented behavior.
