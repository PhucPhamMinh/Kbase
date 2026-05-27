# KBase

KBase is a microservices-based project knowledge base and document management system.

## Folder Structure

```text
.
├── BE
│   ├── api-gateway
│   ├── document-service
│   ├── eureka-server
│   ├── project-service
│   └── user-service
├── FE
│   └── kbase-frontend
├── database
│   └── init
├── .github
│   └── workflows
└── docker-compose.yml
```

## Run Locally

1. Set AWS credentials and S3 bucket values in your shell.
2. Start dependencies and services:

```bash
docker compose up --build
```

3. Open:

- API Gateway: `http://localhost:8080`
- Eureka: `http://localhost:8761`
- PgAdmin: `http://localhost:5050`

The default PostgreSQL credentials are `postgres` / `postgres`.
