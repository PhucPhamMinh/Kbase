# KBase Purge Expired Documents Lambda

This Lambda calls the KBase API endpoint that permanently deletes documents after the 30-day soft delete window.

## Required Lambda Environment Variables

```env
KBASE_API_BASE_URL=http://YOUR_EC2_PUBLIC_IP:8080
KBASE_LAMBDA_PURGE_SECRET=the-same-value-as-LAMBDA_PURGE_SECRET-in-docker-compose
```

## Package Locally

```powershell
cd lambda/purge-expired-documents
npm run check
npm run zip
```

The ZIP file is created at:

```text
lambda/purge-expired-documents/dist/purge-expired-documents.zip
```

## Deploy With AWS CLI

Create an IAM role for Lambda first, then run:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\deploy.ps1 `
  -FunctionName kbase-purge-expired-documents `
  -RoleArn arn:aws:iam::YOUR_ACCOUNT_ID:role/YOUR_LAMBDA_ROLE `
  -ApiBaseUrl http://YOUR_EC2_PUBLIC_IP:8080 `
  -PurgeSecret YOUR_PURGE_SECRET `
  -Region us-east-1
```

## Schedule

Create an EventBridge schedule to invoke this Lambda once per day.
