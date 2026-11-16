---
name: sdlc-qa-self-healing
description: 'Triage failed Library Book Management Catalog tests, determine whether the cause is code or environment, apply bounded fixes, and capture rerun evidence.'
user-invocable: false
---

# QA Failure Triage

1. Read the exact failing command output and identify the failing test, assertion, or environment setup.
2. Reproduce with the narrowest relevant test command before changing code.
3. Make a minimal fix only when evidence identifies a code defect; do not weaken assertions to manufacture a pass.
4. Rerun the failing test and relevant regression checks. Follow the SDLC master policy of at most two fix-and-rerun cycles before escalation.
5. Record original failure, changes, rerun commands, and results in `verification-report.md`.
6. If tools or dependencies cannot run, report that as unverified rather than treating it as success.
