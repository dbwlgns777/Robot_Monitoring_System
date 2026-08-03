$ErrorActionPreference = "Continue"
$repositoryRoot = Split-Path -Parent $PSScriptRoot
$logFile = Join-Path $repositoryRoot "common-domain-test.log"
$resultsDirectory = Join-Path $repositoryRoot "common-domain\build\test-results\test"
$buildExitCode = 1

Push-Location $repositoryRoot
try {
    & .\gradlew.bat :common-domain:test --rerun-tasks --console=plain --stacktrace 2>&1 |
        Tee-Object -FilePath $logFile
    $buildExitCode = $LASTEXITCODE
} finally {
    Pop-Location
}

Write-Host "Gradle test log: $logFile"
$resultFiles = Get-ChildItem -Path $resultsDirectory -Filter "TEST-*.xml" -ErrorAction SilentlyContinue
if (-not $resultFiles) {
    Write-Host "No JUnit XML result was generated. Inspect the Gradle test log above."
    exit $buildExitCode
}

foreach ($resultFile in $resultFiles) {
    Write-Host "JUnit result: $($resultFile.FullName)"
    Select-String -Path $resultFile.FullName `
        -Pattern "<failure|<error|AssertionFailedError|Caused by|Exception" `
        -Context 2,8
}
exit $buildExitCode
