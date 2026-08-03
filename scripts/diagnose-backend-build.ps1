$ErrorActionPreference = "Continue"
$repositoryRoot = Split-Path -Parent $PSScriptRoot
$logFile = Join-Path $repositoryRoot "backend-compile.log"
$buildExitCode = 1

Push-Location $repositoryRoot
try {
    & .\gradlew.bat :backend:compileJava --rerun-tasks --console=plain --stacktrace 2>&1 |
        Tee-Object -FilePath $logFile
    $buildExitCode = $LASTEXITCODE
} finally {
    Pop-Location
}

Write-Host "Backend compiler log: $logFile"
if ($buildExitCode -ne 0) {
    Write-Host "Copy the first compiler 'error:' lines and their file/line references from this log."
}
exit $buildExitCode
