---
name: playwright-cli
description: 'Use Playwright CLI for browser-based end-to-end checks only when a browser UI or browser-accessible flow is part of the approved Library Book Management Catalog scope.'
user-invocable: false
---

# Browser End-to-End Verification

1. Confirm the approved feature includes a browser UI or a browser-required user flow. The current catalog is primarily a REST API; do not invent a frontend test requirement.
2. Check for existing browser tooling and test conventions before proposing new dependencies.
3. Keep Playwright verification under `test-automation/` as specified by the project workflow.
4. Exercise observable user outcomes and relevant validation/error flows; avoid tests coupled to implementation details.
5. Record commands, browser/environment requirements, and actual results in `verification-report.md`.
6. Do not install tooling or start services unless the task and environment explicitly allow it.
