---
name: sdlc
description: 'Use when running the end-to-end SDLC workflow for the Library Book Management Catalog, including requirements, architecture, design review, implementation planning, implementation, code review, verification, and PR preparation.'
argument-hint: 'Run full pipeline, or: from= | resume'
user-invocable: true
---

# SDLC Workflow

## When to Use
- Run the full project lifecycle for this repo
- Resume work after a gate or partial handoff
- Move from requirements into architecture, review, implementation, verification, and PR

## Project baseline
This repository is the Library Book Management Catalog. Use `requirements.md` as the approved requirement baseline and keep the implementation aligned to the Java 17 + Spring Boot + Maven stack described in the repo.

## Procedure
1. Determine the phase to start from and confirm the current state of the artifacts.
2. Use the matching custom agent for the current phase instead of doing the work inline.
3. After each phase, present the gate summary and wait for explicit approval before continuing.
4. Keep a hard boundary between design review rejection and subsequent architecture rework.
5. Only complete the pipeline after verification evidence and a final PR description with changelog.

## Gate rules
- `approve` / `continue` proceeds to the next phase
- `discuss` pauses for clarification and returns to the same gate
- `revise` reruns the active phase with the feedback
- `stop` or `pause` halts the pipeline and requires a resume instruction

## Constraints
- Never skip a gate or proceed without explicit user approval.
- Never switch the project to a hotel app or different domain.
- Keep Python code under `dev/` and verification under `test-automation/`.
- Never claim tests passed without command output.
