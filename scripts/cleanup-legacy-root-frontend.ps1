$ErrorActionPreference = "Stop"
$repositoryRoot = Split-Path -Parent $PSScriptRoot
$frontendPackage = Join-Path $repositoryRoot "frontend\package.json"

if (-not (Test-Path $frontendPackage)) {
    throw "frontend/package.json was not found; refusing to remove root files."
}

$legacyPaths = @(
    "src",
    "node_modules",
    "index.html",
    "package.json",
    "package-lock.json",
    "vite.config.ts",
    "vite.config.js",
    "tsconfig.json",
    "tsconfig.app.json",
    "tsconfig.node.json"
)

foreach ($relativePath in $legacyPaths) {
    $path = Join-Path $repositoryRoot $relativePath
    if (Test-Path $path) {
        Remove-Item -Recurse -Force $path
        Write-Host "Removed legacy root frontend path: $relativePath"
    }
}

Write-Host "Kept the active frontend module: frontend/"
