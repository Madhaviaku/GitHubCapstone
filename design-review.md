# Design Review: Multi-field Book Search

## Review context

- **Design reviewed:** `architecture.md` (search and documentation-sync design)
- **Requirements baseline:** `requirements.md`, especially FR1.4; requested enhancement broadens title-only search to title, author, genre, and ISBN
- **Review date:** 2026-11-12

## Verdict

**Decision:** Approved with conditions

**Rationale:** The existing Spring Boot service/repository layering, `Pageable` pagination, standard `ApiResponse` envelope, and generated-document verification remain a valid fit. No architectural redesign or new datastore is needed. The design is conditionally approved because its search description was title-only, did not define the combined matching/pagination behavior, and did not settle the published search-parameter compatibility choice. The architecture has been updated to capture these points.

## Findings

| ID | Severity | Finding | Evidence | Recommendation | Status |
|---|---|---|---|---|---|
| DR-1 | Medium | Architecture described title-only search despite the all-field requirement. | `requirements.md` FR1.4 requires partial, case-insensitive matching across title, author, genre, and ISBN; `architecture.md` Sections 4.5, 5, and 9 previously said title-based. | Define OR matching across the four fields and apply the predicate before `Pageable` so page results and totals are consistent. | Resolved in `architecture.md` |
| DR-2 | Medium | Search request parameter compatibility was unspecified. | `docs/api.md` currently documents `GET /api/books/search?title=...`; FR1.4 describes a generic search query. | Preserve the current parameter as a generic search-term alias unless an explicit API change authorizes a rename; document the final choice. | Condition before implementation |
| DR-3 | Medium | Documentation must describe the expanded search behavior and continue to be generated/verified, not drift manually. | `docs/api.md` currently states title-only matching; `ApiDocumentationGenerator` emits that text and `ApiDocumentationSyncVerifier` checks generated content. | Update generator output and regenerate `docs/api.md` with the implementation; run the verifier/build to prove sync. Preserve the response envelope and pagination contract. | Condition before completion |
| DR-4 | Low | Leading-wildcard substring search may not benefit from ordinary B-tree indexes as the catalog grows. | Architecture recommends indexes on searchable columns while FR1.4 requires partial matching. | Keep the simple repository implementation for the MVP; inspect query performance if data volume warrants specialized indexing. | Follow-up only |

## Requirements coverage

| Requirement | Covered? | Design evidence | Gap / action |
|---|---|---|---|
| FR1.4 partial, case-insensitive search over title, author, genre, and ISBN | Yes, after update | `architecture.md` Sections 4.2, 4.5, 5, and 9 | Implement the four-field predicate and test partial/case-insensitive matches per field. |
| Pagination for search results | Yes | `architecture.md` Sections 4.2 and 5; repository `Pageable` | Ensure filtering occurs before pagination and totals count only matching records. |
| Standard API response pattern | Yes | `architecture.md` Section 4.2 and response handling section | Keep the existing response envelope and paginated data shape unchanged. |
| API documentation sync | Yes, with implementation condition | `architecture.md` Section 4.7; generator/verifier mechanism | Update generated search text/example and pass the sync verifier. |

## Security, privacy, and operations

This is a design review, not a security audit or runtime verification. Search input should remain a bound query parameter rather than being concatenated into SQL. No new sensitive data or authorization assumptions are introduced. Substring-search performance should be monitored if catalog size grows.

## Conditions and follow-up

- Before implementation, confirm whether the existing `title` request parameter will remain as the generic search-term alias (recommended for compatibility) or whether an explicitly approved API contract change will rename it.
- In implementation, cover each searchable field, partial matching, case-insensitivity, empty/no-match behavior, and pagination totals; keep controller logic thin and business/query orchestration in `BookService`/repository.
- Regenerate `/docs/api.md` with `ApiDocumentationGenerator` and verify it with `ApiDocumentationSyncVerifier` after updating the endpoint contract.

## Re-review criteria

No architecture re-review is required if the implementation follows the clarified contract, preserves the existing response and pagination shapes, and passes documentation synchronization. Re-review if the API parameter or response schema changes, or if the search implementation requires a new persistence/search subsystem.
