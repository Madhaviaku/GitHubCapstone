$ErrorActionPreference = 'Stop'
$null = [Console]::In.ReadToEnd()

@{
    systemMessage = 'This workspace is the Library Book Management Catalog. Follow requirements.md, preserve the gated SDLC workflow, and never claim verification without executed evidence.'
} | ConvertTo-Json -Compress | Write-Output
