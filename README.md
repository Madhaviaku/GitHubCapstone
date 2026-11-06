# Github Capstone Project

**Agentic SDLC Use Case: Automated Documentation Sync**

Team exercise for GitHub Copilot.

## Project overview

This capstone builds an Agentic SDLC pipeline from scratch with GitHub Copilot. The software delivery lifecycle is driven through Copilot Chat, agent mode, inline suggestions, custom instructions, and the Copilot CLI as each step requires them.

The exercise covers the full delivery path: write requirements, design the system, review the design, plan the work, implement it, review the code, verify it, and merge a production-ready pull request. GitHub Copilot is the AI pair programmer at every step.

The product built in this repository is a Library Book Management Catalog. Library administrators can create, read, update, delete, and search books. The build keeps `/docs/api.md` aligned with the code and fails when the documentation is out of date.

The source user story is Jira epic [EPMCDMETST-25](https://rd-training-oct-batch.atlassian.net/browse/EPMCDMETST-25).

## Step 1: Requirements

Use GitHub Copilot Chat to define and document the functional and non-functional requirements for the user story.

- Read the user story from Jira, Confluence, or a word document.
- Let Copilot ask clarifying questions, and answer those questions.
- Capture the agreed requirements in `requirements.md` and commit that file.

## Step 2: Architecture

Use GitHub Copilot Chat to design the high-level system architecture. Ask Copilot to propose component diagrams, technology choices, and data flow.

- Ask for an architecture recommendation based on `requirements.md`.
- Document the proposed architecture in `architecture.md`.
- Identify the key components and their responsibilities.

## Step 3: Design review

Use GitHub Copilot Chat to review the architecture before writing production code. Treat Copilot as a senior reviewer.

- Share `architecture.md` with Copilot Chat and ask it to identify risks and gaps.
- Document review findings and agreed design decisions in `design-review.md`.
- Update `architecture.md` when the review finds issues.

## Step 4: Implementation planning

Use GitHub Copilot Chat to break the approved architecture into a prioritised, dependency-ordered task list.

- Ask Copilot to generate a task breakdown from `architecture.md`.
- Document the plan in `impl-plan.md`, ordered by dependency.
- Identify any task that cannot start until another task finishes.

## Step 5: Implementation

Use GitHub Copilot to implement the changes Copilot suggests and a human approves.

Use the GitHub Copilot features that fit this step, including Chat, agent mode, inline suggestions, and the Copilot CLI. Project instructions live in `.github/copilot-instructions.md`.

## Step 6: Review

Use GitHub Copilot Chat or a Copilot code review to review the implementation before opening a pull request. Copilot acts as a peer reviewer.

| Review area | Review question |
| --- | --- |
| Correctness | Does each component behave as specified in `requirements.md`? |
| Security | Are secrets excluded from output? Is user input validated? |
| Error handling | Are API failures, missing files, and empty repos handled gracefully? |
| Test coverage | Do tests cover the happy path and the Not Found / missing-field edge cases? |
| Code clarity | Are function names self-explanatory? Is the logic easy to follow without comments? |
| DRY principle | Is there duplicated logic that Copilot can refactor into a shared function? |
| Dependency safety | Does Copilot flag any known vulnerable package versions? |

Record the review in `code review.md`.

## Step 7: Verify

Use GitHub Copilot to generate and run a verification suite. Verify both the code (unit and integration tests) and the final output document (content quality).

Record the results in `verification-report.md`.

```text
mvn test
```

## Step 8: Pull request

Use GitHub Copilot and the GitHub CLI to open the pull request. The pull request description should include:

- **What was built and why.**
- **Changes made.** A bulleted list of every file added or modified, and the reason.
- **Test evidence.** The test run output, or a link to CI results.
- **Known limitations.** Anything marked Not Found, or left out of scope.
- **Reviewer checklist.** A tick-list the reviewer completes before approving.

The draft for this repository is in `Final PR.md`.

## Run the catalog

Java 17 and Maven are required.

```text
mvn spring-boot:run
```

The API listens on port 8080. Endpoints are documented in `docs/api.md`.

## Read a Jira issue

The `org.example.Main` command reads one Jira Cloud issue and prints its key, summary, status, and assignee.

Set these environment variables in the shell. Do not commit the API token.

- `JIRA_BASE_URL`: `https://rd-training-oct-batch.atlassian.net`
- `JIRA_EMAIL`: the Atlassian account email
- `JIRA_API_TOKEN`: an API token for that account

```text
mvn compile exec:java -Dexec.mainClass=org.example.Main -Dexec.args="EPMCDMETST-25"
```

The account must have permission to browse the issue.
