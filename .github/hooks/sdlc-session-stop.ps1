$ErrorActionPreference = 'Stop'
$null = [Console]::In.ReadToEnd()

@{
    systemMessage = 'Before concluding an SDLC task, state what was changed, identify any verification that was not run, and do not report unverified tests as passing.'
} | ConvertTo-Json -Compress | Write-Output
