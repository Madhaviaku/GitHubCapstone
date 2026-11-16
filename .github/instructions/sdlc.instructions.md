---
description: Apply the gated SDLC workflow and repository boundaries to feature work.
applyTo: "**/*"
---

# Gated SDLC Instructions

Follow the gated flow in order: requirements -> architecture -> design review -> implementation plan -> implementation -> review -> verification -> PR. Read the relevant artifact before acting. If an artifact is missing, start from the matching template in [.github/templates](.github/templates).

Each handoff requires an explicit response: approve, discuss, revise, or stop. If a design is rejected, return to architecture. Stay within the approved Library Book Management Catalog scope in `requirements.md`; state uncertainty rather than filling gaps.

Keep implementation aligned with the Java 17 and Spring Boot app structure under `src/main/` and `src/test/`. Keep any browser verification separate from implementation work and within approved scope.

## Jira and MCP access

Use the Atlassian MCP server from [.vscode/mcp.json](.vscode/mcp.json) for Jira links or issue keys. Do not add tokens or Authorization headers to that file. If Jira is unavailable, report whether the blocker is configuration, authentication, or tool discovery; do not invent issue details.

If auth errors appear, clear the stale session, reload VS Code, restart the server, and retry. Do not claim files were read, edited, or committed when the workspace capability is unavailable.