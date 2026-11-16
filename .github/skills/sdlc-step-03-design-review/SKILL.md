---
name: sdlc-step-03-design-review
description: 'Review architecture.md against approved catalog requirements for coverage, correctness, risks, security assumptions, tradeoffs, and implementation readiness.'
user-invocable: false
---

# Phase 3: Design Review

1. Compare `architecture.md` with `requirements.md` and the Jira-approved scope.
2. Record evidence-backed gaps, risks, and recommendations; distinguish blockers from follow-up improvements.
3. Use [the design review template](../../templates/design-review-template.md) for `design-review.md`.
4. Give an explicit Approved, Approved with conditions, or Rejected verdict.
5. If rejected, identify exact architecture changes required and route back to Phase 2; do not proceed to planning.

The phase agent is `.github/agents/sdlc-step-03-design-review.agent.md`.
