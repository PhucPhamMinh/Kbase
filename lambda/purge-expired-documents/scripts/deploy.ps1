param(
    [Parameter(Mandatory = $true)]
    [string]$FunctionName,

    [Parameter(Mandatory = $true)]
    [string]$RoleArn,

    [Parameter(Mandatory = $true)]
    [string]$ApiBaseUrl,

    [Parameter(Mandatory = $true)]
    [string]$PurgeSecret,

    [string]$Region = "us-east-1"
)

$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$ZipPath = Join-Path $ProjectRoot "dist\purge-expired-documents.zip"

powershell -ExecutionPolicy Bypass -File (Join-Path $PSScriptRoot "package.ps1")

$ExistingFunction = aws lambda get-function `
    --function-name $FunctionName `
    --region $Region `
    2>$null

if ($LASTEXITCODE -eq 0) {
    aws lambda update-function-code `
        --function-name $FunctionName `
        --zip-file "fileb://$ZipPath" `
        --region $Region | Out-Null
} else {
    aws lambda create-function `
        --function-name $FunctionName `
        --runtime nodejs20.x `
        --role $RoleArn `
        --handler index.handler `
        --zip-file "fileb://$ZipPath" `
        --timeout 30 `
        --memory-size 128 `
        --region $Region | Out-Null
}

aws lambda update-function-configuration `
    --function-name $FunctionName `
    --environment "Variables={KBASE_API_BASE_URL=$ApiBaseUrl,KBASE_LAMBDA_PURGE_SECRET=$PurgeSecret}" `
    --region $Region | Out-Null

Write-Host "Deployed Lambda function $FunctionName"
