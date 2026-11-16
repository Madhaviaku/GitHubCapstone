# Implementation Plan: Multi-field Catalog Search

## Goal and scope

Implement the approved FR1.4 search enhancement: partial, case-insensitive matching across book title, author, genre, and ISBN. Keep `GET /api/books/search`, retain the published `title` query parameter as the generic search-term alias, and preserve the existing page/size behavior, required/non-blank search validation, response envelope, and paginated `data` shape. Update generated API documentation and prove it is synchronized.

This plan is limited to search behavior and its directly related tests and documentation. It does not change CRUD behavior, the API response schema, validation rules for book fields, or introduce a new search subsystem or datastore.

## Preconditions and dependencies

- `requirements.md` is approved; FR1.4 is the controlling requirement for this enhancement.
- `architecture.md` Sections 4.2, 4.3, 4.5, 4.7, and 9 describe the approved contract and implementation boundaries.
- `design-review.md` approves the architecture with conditions. It recommends retaining the existing `title` parameter as a generic search-term alias, but requires that compatibility choice to be explicitly confirmed before implementation. Its documentation-sync condition remains to be completed during implementation.
- The plan is ready for the plan-approval gate. The approver must confirm the recommended `title` alias choice; do not start implementation until that decision and this plan are approved.

## Ordered work items

| Order | Task | Requirement(s) | Dependencies | Likely files / components | Completion evidence |
|---|---|---|---|---|---|
| 1 | Lock the search contract and scope | FR1.4, FR2.2, FR3.1 | None | `requirements.md`, `architecture.md`, `design-review.md`, current `/api/books/search` contract | Obtain explicit approval to retain `title` as the required, non-blank generic search-term parameter (the architecture recommendation). Preserve current `page`/`size` defaults and constraints, response envelope, page shape, and existing error handling. Any proposed parameter or schema change returns to design review. |
| 2 | Implement the repository multi-field predicate | FR1.4 | 1 | `src\main\java\com\library\catalog\repository\BookRepository.java` | Add a parameterized OR predicate for partial, case-insensitive title, author, genre, and ISBN matching. Return a `Page` with the predicate applied before pagination, consistent matching totals, and the existing created-date ordering. Repository-level tests cover each field and verify page content/totals. |
| 3 | Update service search orchestration and retain validation | FR1.4, FR2.2, FR3.1 | 2 | `src\main\java\com\library\catalog\service\BookService.java` | Normalize the supplied search term and delegate to the new repository query. Keep blank/missing search-term and invalid pagination outcomes as client errors; do not move business/query rules into the controller or alter unrelated validation. Service tests cover trimming, blank input, and valid paginated searches. |
| 4 | Verify the endpoint contract and search behavior | FR1.4, FR2.2, FR3.1 | 3 | `src\main\java\com\library\catalog\controller\BookController.java`, `src\test\java\com\library\catalog\BookControllerIntegrationTest.java` | Exercise partial and case-insensitive matches independently for title, author, genre, and ISBN; OR matching; no matches; required/blank query; invalid page/size; and pagination totals/page boundaries. Assert the existing `title`, `page`, and `size` parameters, HTTP status, response envelope, and paginated data shape remain unchanged. Keep controller logic thin. |
| 5 | Update and regenerate search API documentation | FR4.1, FR4.3 | 1, 4 | `src\main\java\com\library\catalog\ApiDocumentationGenerator.java`, `docs\api.md` | Update generated endpoint text to explain partial, case-insensitive matching across all four fields while documenting `title` as the required generic search-term alias, current pagination defaults/constraints, response shape, and errors. Regenerate `docs\api.md` with `ApiDocumentationGenerator`; do not hand-edit generated output as the source of truth. |
| 6 | Run documentation sync and full verification | FR1.4, FR2.2, FR3.1, FR4.2 | 2, 3, 4, 5 | `ApiDocumentationSyncVerifier`, Maven build, search tests | Run the Maven tests and build, including the documentation sync verifier. Confirm generated documentation matches `docs/api.md`, search test cases pass, and no existing tests or API contracts regress. Record command/output evidence for the verification report. |
| 7 | Review and prepare release handoff | FR1.4, FR4 | 6 | Changed files, `code review.md`, `Final PR.md` | Review the diff against the approved contract and requirements; resolve or explicitly accept findings. Summarize changed files, verification evidence, and known limitations (including substring-search indexing/performance if relevant) for the PR/release handoff. |

## Verification plan

| Layer | Scenarios | Command / evidence |
|---|---|---|
| Repository / unit | Partial and case-insensitive matching for each of the four fields; OR semantics; no matches; correct page content, order, and filtered totals | Relevant repository/service tests, then `mvn test` |
| Integration / API | Existing `title` parameter and pagination defaults; missing or blank term; invalid pagination; response envelope and page schema unchanged | `BookControllerIntegrationTest` and `mvn test` |
| Documentation | Generator describes all-field search and the retained parameter; committed documentation equals generated content | `mvn compile exec:java -Dexec.mainClass=com.library.catalog.ApiDocumentationGenerator`, then run the verifier through `mvn verify` |
| Regression / review | Existing CRUD, validation, and error handling remain unchanged; query is parameter-bound; diff stays within search scope | `mvn verify` plus code review against `requirements.md`, `architecture.md`, and `design-review.md` |

## Risks and mitigations

| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|
| Legacy title-only wording elsewhere in the older requirements text conflicts with FR1.4 | Medium | Medium | Follow FR1.4 as identified by the design review and the updated approved architecture. If stakeholders dispute that baseline, pause for a requirements/design decision rather than silently reverting or expanding scope. |
| OR matching or predicate placement produces incorrect page totals or duplicate-looking results | Low | Medium | Test matching across every field and assert that pagination and total counts are calculated over the combined predicate before paging. |
| Leading-wildcard matching may not use ordinary B-tree indexes efficiently as the catalog grows | Medium | Low for MVP | Keep the approved simple repository implementation for the MVP; monitor query plans and revisit specialized indexing only if measured catalog scale warrants it. |
| Generated docs drift from the endpoint contract | Medium | Medium | Update the generator, regenerate `docs/api.md`, and require the sync verifier/build to pass before review completion. |

## Definition of done

- [ ] FR1.4 behavior is implemented for partial, case-insensitive matching across title, author, genre, and ISBN.
- [ ] Existing endpoint parameter compatibility, validation, pagination, response envelope, and paginated data shape are preserved.
- [ ] Unit and API tests cover each searchable field, edge cases, and pagination totals.
- [ ] `docs/api.md` is regenerated with `ApiDocumentationGenerator` and passes `ApiDocumentationSyncVerifier` through the Maven build.
- [ ] Maven verification passes and evidence is recorded.
- [ ] Code review findings are resolved or explicitly accepted; release/PR handoff includes changed files and known limitations.

## Open questions and blockers

- **Pre-implementation decision required:** confirm that the published `title` parameter remains the generic search-term alias, as recommended by the architecture. If not confirmed, return to design review before implementation.
- **Approval gate:** implementation must wait for explicit approval of this plan. Reopen design review if implementation would rename the parameter, alter the response schema, or add a new search subsystem.
