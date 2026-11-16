# catalog: Expand book search and align SDLC workflow

## Why

Improve catalog search while preserving the existing endpoint contract, complete the related request-validation and immutable-ISBN behavior, and provide repository-specific gated SDLC guidance for this Java/Spring Boot catalog.

## What changed

- `GET /api/books/search` now performs paginated, partial, case-insensitive matching across title, author, genre, and ISBN. It keeps the required `title` query parameter, existing pagination and response shape, and removes hyphens from the search term for ISBN matching.
- Create/update request validation and quantity defaults are tightened, ISBN cannot be changed through update, and malformed/incomplete requests return structured client errors.
- Maven verifies generated API documentation against `docs/api.md`.
- Adds a Jira-aware, human-gated SDLC workflow with phase skills/agents, templates, and lifecycle hooks, aligned to the Library Book Management Catalog and its Java/Spring Boot stack.

## Changes made

### Catalog implementation and verification

- `pom.xml` — wires API documentation synchronization verification into Maven.
- `src/main/java/com/library/catalog/ApiDocumentationGenerator.java` — documents the current API and search contract.
- `src/main/java/com/library/catalog/dto/BookCreateRequest.java` — tightens create-field validation and supports the quantity default.
- `src/main/java/com/library/catalog/dto/BookUpdateRequest.java` — disallows ISBN changes in update requests.
- `src/main/java/com/library/catalog/exception/GlobalExceptionHandler.java` — maps malformed/incomplete request errors to structured client responses.
- `src/main/java/com/library/catalog/repository/BookRepository.java` — implements paginated OR matching across searchable fields.
- `src/main/java/com/library/catalog/service/BookService.java` — validates and normalizes search input and applies catalog business rules.
- `src/test/java/com/library/catalog/BookControllerIntegrationTest.java` — covers endpoint behavior, response shape, validation, and multi-field search.
- `src/test/java/com/library/catalog/BookServiceTest.java` — covers service-level search and catalog rules.

### SDLC workspace customization

- `.github/agents/sdlc.agent.md` — removes the prior SDLC agent definition.
- `.github/agents/sdlcmaster.agent.md` — adds the Jira-aware SDLC coordinator and phase mapping.
- `.github/hooks/sdlc-hooks.json` — configures lifecycle hooks.
- `.github/hooks/sdlc-prompt-check.ps1` — checks prompts against workflow boundaries.
- `.github/hooks/sdlc-session-start.ps1` — adds session-start workflow guidance.
- `.github/hooks/sdlc-session-stop.ps1` — adds session-stop guidance.
- `.github/instructions/sdlc.instructions.md` — applies catalog-specific gated workflow instructions.
- `.github/skills/playwright-cli/SKILL.md` — documents conditional browser verification.
- `.github/skills/sdlc-qa-self-healing/SKILL.md` — documents bounded test-failure triage.
- `.github/skills/sdlc-step-01-requirements/SKILL.md` — defines the requirements phase.
- `.github/skills/sdlc-step-02-architecture/SKILL.md` — defines the architecture phase.
- `.github/skills/sdlc-step-03-design-review/SKILL.md` — defines the design-review phase.
- `.github/skills/sdlc-step-04-impl-plan/SKILL.md` — defines the implementation-planning phase.
- `.github/skills/sdlc-step-05-implementation/SKILL.md` — defines the implementation phase.
- `.github/skills/sdlc-step-06-review/SKILL.md` — defines the code-review phase.
- `.github/skills/sdlc-step-07-verify/SKILL.md` — defines the verification phase.
- `.github/skills/sdlc-step-08-pr/SKILL.md` — defines the PR preparation phase.
- `.github/skills/sdlc/SKILL.md` — defines the Jira-driven, gated end-to-end workflow.
- `.github/templates/architecture-template.md` — structures architecture artifacts.
- `.github/templates/design-review-template.md` — structures design reviews.
- `.github/templates/impl-plan-template.md` — structures implementation plans.
- `.github/templates/jira-sdlc-analysis-template.md` — structures Jira assessments.
- `.github/templates/requirements-template.md` — structures requirements artifacts.
- `.github/copilot-instructions.md` — documents repository product, stack, and workflow boundaries.
- `.gitignore` — updates generated/local artifact exclusions.

### Requirements, design, API docs, and delivery evidence

- `requirements.md` — records catalog requirements, including multi-field search and validation.
- `architecture.md` — documents search behavior and API architecture.
- `design-review.md` — records design findings and decisions.
- `impl-plan.md` — records dependency-ordered implementation and verification work.
- `code review.md` — records implementation review observations.
- `docs/api.md` — synchronizes the generated API reference with the implementation.
- `verification-report.md` — records Maven test and API documentation synchronization evidence.
- `CHANGELOG.md` — records the catalog and SDLC workflow changes.
- `Final PR.md` — documents this combined PR for reviewers.

## Test evidence

The executed `mvn clean test` run completed with **BUILD SUCCESS**: 12 tests, 0 failures, 0 errors, and 0 skipped. The configured API documentation sync verifier printed `API documentation is in sync.` See `verification-report.md`. Maven warnings were reported but did not fail the build.

## Known limitations

- Search is substring-based and not relevance-ranked; there is no dedicated search index or performance/load-test evidence.
- Search results retain existing ordering and pagination; ISBN search removes hyphens but does not normalize other punctuation.
- The recorded test suite does not establish dedicated coverage for every blank-input and pagination edge case.
- This PR includes both catalog behavior and SDLC workspace customizations; reviewers should consider them as two related repository deliverables.

## Release and rollback

No schema migration or new runtime configuration is introduced by the documented changes. Deploy using the normal application process and verify the search endpoint and documentation check. To roll back, redeploy the previous application version and revert the associated API documentation, workflow guidance, and changelog changes. No feature flag or persisted search state is involved.

## Reviewer checklist

- [ ] Confirm search matches title, author, genre, and ISBN case-insensitively while preserving the existing query parameter, pagination, and response contract.
- [ ] Confirm validation/default behavior and ISBN immutability match the requirements.
- [ ] Confirm `docs/api.md` matches generated API documentation.
- [ ] Review the recorded Maven test and API documentation sync evidence.
- [ ] Confirm SDLC agents, skills, hooks, and templates are specific to this Java/Spring Boot catalog and honor the approval gates.
- [ ] Confirm all changes in this combined PR are intended, including both catalog and SDLC customization work.
- [ ] Review release and rollback considerations.

## Review status

Prepared for review. No PR approval or release authorization is asserted.
