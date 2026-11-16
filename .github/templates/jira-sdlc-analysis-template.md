# Jira SDLC Assessment: <ISSUE-KEY>

## Decision summary

- **Issue:** <key, title, and Jira URL>
- **Status / priority:** <ticket values or Not available>
- **Recommendation:** <Proceed to requirements approval / Clarify first / Blocked / Out of scope>
- **Confidence:** <High / Medium / Low, with reason>
- **Top blockers:** <None identified or prioritized list>

## Ticket review and evidence

| Source | Finding | Evidence / limitation |
|---|---|---|
| Jira | <ticket fact> | <section/comment or access limitation> |
| Repository | <baseline/current behavior> | <file and line or symbol> |

## Requirement analysis

### User outcome
<Who needs what and why, based on the ticket.>

### Functional requirements

| ID | Requirement | Source | Status |
|---|---|---|---|
| REQ-1 | <requirement> | <Jira or repository evidence> | <Confirmed / Assumption / Question> |

### Non-functional requirements
<Performance, compatibility, accessibility, reliability, privacy, operational requirements. Mark unknown items clearly.>

### Acceptance criteria

| ID | Given / When / Then or observable criterion | Verification |
|---|---|---|
| AC-1 | <criterion> | <test or evidence> |

### Scope and traceability

| Requirement | Acceptance criterion | Impacted component(s) | Verification |
|---|---|---|---|
| <REQ-ID> | <AC-ID> | <code/API/docs/data> | <test/check> |

**Out of scope / baseline conflicts:** <List conflicts with `requirements.md` or “None identified”.>

## Development plan

| Order | Work item | Likely component(s) | Dependency | Completion evidence |
|---|---|---|---|---|
| 1 | <task> | <files/components> | <none or prerequisite> | <observable result> |

**Definition of done:** <Requirements, tests, docs, review, and build conditions.>

## Testing strategy

| Layer | Scenarios | Evidence / command |
|---|---|---|
| Unit | <business logic and edge cases> | <test target> |
| API / integration | <request, response, persistence, error cases> | <test target> |
| Regression | <existing behavior to preserve> | <test target> |
| Documentation / build | <API doc sync and build checks> | <generator/verifier/build> |
| Security / operations | <relevant checks> | <tool or manual evidence> |

## Risk assessment

| Risk | Likelihood | Impact | Rationale | Mitigation | Owner |
|---|---|---|---|---|---|
| <risk> | <Low/Medium/High> | <Low/Medium/High> | <evidence-based reason> | <action> | <role or TBD> |

## Security review

- **Data sensitivity / privacy:** <assessment or unknown>
- **Input validation / authorization / abuse cases:** <applicable analysis>
- **Dependencies / secrets / logging:** <applicable analysis>
- **Security checks performed:** <exact checks performed, or “None; planning-level review only”>
- **Findings and required mitigations:** <findings or “None identified from reviewed evidence”; distinguish unverified areas>

## Blockers and open questions

| Priority | Question / blocker | Why it matters | Needed from |
|---|---|---|---|
| <P0/P1/P2> | <missing decision or information> | <implementation/test/release impact> | <role or TBD> |

## Release recommendations

- **Readiness recommendation:** <Proceed / Hold / Not assessable>
- **Pre-release criteria:** <tests, documentation, approvals, migration/config>
- **Rollout and rollback:** <recommendations or unknowns>
- **Monitoring / support:** <signals and owner or TBD>
- **Release notes:** <user-facing change and known limitations>

## Assumptions and limitations

- <Explicit assumptions, access limitations, repository gaps, and conclusions that could not be verified.>
