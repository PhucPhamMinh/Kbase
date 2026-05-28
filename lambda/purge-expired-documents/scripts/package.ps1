$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$DistDir = Join-Path $ProjectRoot "dist"
$ZipPath = Join-Path $DistDir "purge-expired-documents.zip"

if (Test-Path $DistDir) {
    Remove-Item -LiteralPath $DistDir -Recurse -Force
}

New-Item -ItemType Directory -Path $DistDir | Out-Null
Compress-Archive -Path (Join-Path $ProjectRoot "index.mjs") -DestinationPath $ZipPath -Force

Write-Host "Created $ZipPath"
