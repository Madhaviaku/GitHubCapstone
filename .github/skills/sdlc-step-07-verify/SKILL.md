---
name: sdlc-step-07-verify
description: 'Run and document evidence-based verification for Library Book Management Catalog changes, including Maven tests, API behavior, and API documentation sync.'
user-invocable: false
---

# Phase 7: Verification

1. Derive tests from approved acceptance criteria and code-review findings.
2. Run the smallest relevant Maven verification first, then broaden when needed.
3. Verify API documentation with the repository generator/verifier when applicable.
4. Capture exact commands, outcomes, and environment limitations in `verification-report.md`.
5. If tests fail, distinguish product defects from infrastructure/tool failures. Retry fixes only within the master agent's bounded policy.
6. Do not claim success without actual command output.

The phase agent is `.github/agents/sdlc-step-07-verify.agent.md`.
