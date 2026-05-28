# Details

Date : 2026-05-28 09:10:25

Directory c:\\BachKhoa\\Kbase

Total : 118 files,  11779 codes, 166 comments, 814 blanks, all 12759 lines

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [.github/workflows/deploy.yml](/.github/workflows/deploy.yml) | YAML | 40 | 0 | 9 | 49 |
| [BE/api-gateway/Dockerfile](/BE/api-gateway/Dockerfile) | Docker | 10 | 0 | 2 | 12 |
| [BE/api-gateway/pom.xml](/BE/api-gateway/pom.xml) | XML | 68 | 0 | 1 | 69 |
| [BE/api-gateway/src/main/java/com/kbase/gateway/ApiGatewayApplication.java](/BE/api-gateway/src/main/java/com/kbase/gateway/ApiGatewayApplication.java) | Java | 11 | 0 | 3 | 14 |
| [BE/api-gateway/src/main/java/com/kbase/gateway/security/JwtAuthenticationFilter.java](/BE/api-gateway/src/main/java/com/kbase/gateway/security/JwtAuthenticationFilter.java) | Java | 65 | 0 | 10 | 75 |
| [BE/api-gateway/src/main/resources/application.yml](/BE/api-gateway/src/main/resources/application.yml) | YAML | 62 | 0 | 5 | 67 |
| [BE/document-service/Dockerfile](/BE/document-service/Dockerfile) | Docker | 10 | 0 | 2 | 12 |
| [BE/document-service/pom.xml](/BE/document-service/pom.xml) | XML | 34 | 0 | 1 | 35 |
| [BE/document-service/src/main/java/com/kbase/document/DocumentServiceApplication.java](/BE/document-service/src/main/java/com/kbase/document/DocumentServiceApplication.java) | Java | 11 | 0 | 3 | 14 |
| [BE/document-service/src/main/java/com/kbase/document/common/ResponseTemplate.java](/BE/document-service/src/main/java/com/kbase/document/common/ResponseTemplate.java) | Java | 10 | 0 | 4 | 14 |
| [BE/document-service/src/main/java/com/kbase/document/config/RestClientConfig.java](/BE/document-service/src/main/java/com/kbase/document/config/RestClientConfig.java) | Java | 11 | 0 | 3 | 14 |
| [BE/document-service/src/main/java/com/kbase/document/config/S3Config.java](/BE/document-service/src/main/java/com/kbase/document/config/S3Config.java) | Java | 18 | 0 | 4 | 22 |
| [BE/document-service/src/main/java/com/kbase/document/controller/DocumentController.java](/BE/document-service/src/main/java/com/kbase/document/controller/DocumentController.java) | Java | 71 | 0 | 10 | 81 |
| [BE/document-service/src/main/java/com/kbase/document/dto/DocumentReadUrlResponse.java](/BE/document-service/src/main/java/com/kbase/document/dto/DocumentReadUrlResponse.java) | Java | 4 | 0 | 3 | 7 |
| [BE/document-service/src/main/java/com/kbase/document/dto/DocumentResponse.java](/BE/document-service/src/main/java/com/kbase/document/dto/DocumentResponse.java) | Java | 14 | 0 | 2 | 16 |
| [BE/document-service/src/main/java/com/kbase/document/dto/ProjectPermissionResponse.java](/BE/document-service/src/main/java/com/kbase/document/dto/ProjectPermissionResponse.java) | Java | 11 | 0 | 2 | 13 |
| [BE/document-service/src/main/java/com/kbase/document/dto/UpdateDocumentRequest.java](/BE/document-service/src/main/java/com/kbase/document/dto/UpdateDocumentRequest.java) | Java | 8 | 0 | 3 | 11 |
| [BE/document-service/src/main/java/com/kbase/document/exception/BadRequestException.java](/BE/document-service/src/main/java/com/kbase/document/exception/BadRequestException.java) | Java | 6 | 0 | 2 | 8 |
| [BE/document-service/src/main/java/com/kbase/document/exception/GlobalExceptionHandler.java](/BE/document-service/src/main/java/com/kbase/document/exception/GlobalExceptionHandler.java) | Java | 33 | 0 | 6 | 39 |
| [BE/document-service/src/main/java/com/kbase/document/exception/ResourceNotFoundException.java](/BE/document-service/src/main/java/com/kbase/document/exception/ResourceNotFoundException.java) | Java | 6 | 0 | 2 | 8 |
| [BE/document-service/src/main/java/com/kbase/document/model/Document.java](/BE/document-service/src/main/java/com/kbase/document/model/Document.java) | Java | 46 | 0 | 14 | 60 |
| [BE/document-service/src/main/java/com/kbase/document/repository/DocumentRepository.java](/BE/document-service/src/main/java/com/kbase/document/repository/DocumentRepository.java) | Java | 7 | 0 | 3 | 10 |
| [BE/document-service/src/main/java/com/kbase/document/service/DocumentService.java](/BE/document-service/src/main/java/com/kbase/document/service/DocumentService.java) | Java | 99 | 0 | 13 | 112 |
| [BE/document-service/src/main/java/com/kbase/document/service/ProjectPermissionClient.java](/BE/document-service/src/main/java/com/kbase/document/service/ProjectPermissionClient.java) | Java | 57 | 0 | 9 | 66 |
| [BE/document-service/src/main/java/com/kbase/document/service/S3StorageService.java](/BE/document-service/src/main/java/com/kbase/document/service/S3StorageService.java) | Java | 84 | 0 | 10 | 94 |
| [BE/document-service/src/main/resources/application.properties](/BE/document-service/src/main/resources/application.properties) | Java Properties | 17 | 0 | 7 | 24 |
| [BE/eureka-server/Dockerfile](/BE/eureka-server/Dockerfile) | Docker | 10 | 0 | 2 | 12 |
| [BE/eureka-server/pom.xml](/BE/eureka-server/pom.xml) | XML | 42 | 0 | 1 | 43 |
| [BE/eureka-server/src/main/java/com/kbase/eureka/EurekaServerApplication.java](/BE/eureka-server/src/main/java/com/kbase/eureka/EurekaServerApplication.java) | Java | 11 | 0 | 3 | 14 |
| [BE/eureka-server/src/main/resources/application.properties](/BE/eureka-server/src/main/resources/application.properties) | Java Properties | 5 | 0 | 2 | 7 |
| [BE/project-service/Dockerfile](/BE/project-service/Dockerfile) | Docker | 10 | 0 | 2 | 12 |
| [BE/project-service/pom.xml](/BE/project-service/pom.xml) | XML | 34 | 0 | 1 | 35 |
| [BE/project-service/src/main/java/com/kbase/project/ProjectServiceApplication.java](/BE/project-service/src/main/java/com/kbase/project/ProjectServiceApplication.java) | Java | 11 | 0 | 3 | 14 |
| [BE/project-service/src/main/java/com/kbase/project/common/ResponseTemplate.java](/BE/project-service/src/main/java/com/kbase/project/common/ResponseTemplate.java) | Java | 10 | 0 | 4 | 14 |
| [BE/project-service/src/main/java/com/kbase/project/controller/ProjectController.java](/BE/project-service/src/main/java/com/kbase/project/controller/ProjectController.java) | Java | 85 | 0 | 15 | 100 |
| [BE/project-service/src/main/java/com/kbase/project/dto/AddMemberRequest.java](/BE/project-service/src/main/java/com/kbase/project/dto/AddMemberRequest.java) | Java | 12 | 0 | 3 | 15 |
| [BE/project-service/src/main/java/com/kbase/project/dto/CreateProjectRequest.java](/BE/project-service/src/main/java/com/kbase/project/dto/CreateProjectRequest.java) | Java | 5 | 0 | 3 | 8 |
| [BE/project-service/src/main/java/com/kbase/project/dto/InviteMemberRequest.java](/BE/project-service/src/main/java/com/kbase/project/dto/InviteMemberRequest.java) | Java | 12 | 0 | 3 | 15 |
| [BE/project-service/src/main/java/com/kbase/project/dto/ProjectInvitationResponse.java](/BE/project-service/src/main/java/com/kbase/project/dto/ProjectInvitationResponse.java) | Java | 17 | 0 | 3 | 20 |
| [BE/project-service/src/main/java/com/kbase/project/dto/ProjectMemberResponse.java](/BE/project-service/src/main/java/com/kbase/project/dto/ProjectMemberResponse.java) | Java | 13 | 0 | 3 | 16 |
| [BE/project-service/src/main/java/com/kbase/project/dto/ProjectPermissionResponse.java](/BE/project-service/src/main/java/com/kbase/project/dto/ProjectPermissionResponse.java) | Java | 11 | 0 | 2 | 13 |
| [BE/project-service/src/main/java/com/kbase/project/dto/ProjectResponse.java](/BE/project-service/src/main/java/com/kbase/project/dto/ProjectResponse.java) | Java | 3 | 0 | 2 | 5 |
| [BE/project-service/src/main/java/com/kbase/project/dto/UpdateProjectRequest.java](/BE/project-service/src/main/java/com/kbase/project/dto/UpdateProjectRequest.java) | Java | 5 | 0 | 3 | 8 |
| [BE/project-service/src/main/java/com/kbase/project/exception/BadRequestException.java](/BE/project-service/src/main/java/com/kbase/project/exception/BadRequestException.java) | Java | 6 | 0 | 2 | 8 |
| [BE/project-service/src/main/java/com/kbase/project/exception/ForbiddenException.java](/BE/project-service/src/main/java/com/kbase/project/exception/ForbiddenException.java) | Java | 6 | 0 | 2 | 8 |
| [BE/project-service/src/main/java/com/kbase/project/exception/GlobalExceptionHandler.java](/BE/project-service/src/main/java/com/kbase/project/exception/GlobalExceptionHandler.java) | Java | 34 | 0 | 7 | 41 |
| [BE/project-service/src/main/java/com/kbase/project/exception/ResourceNotFoundException.java](/BE/project-service/src/main/java/com/kbase/project/exception/ResourceNotFoundException.java) | Java | 6 | 0 | 2 | 8 |
| [BE/project-service/src/main/java/com/kbase/project/model/InvitationStatus.java](/BE/project-service/src/main/java/com/kbase/project/model/InvitationStatus.java) | Java | 6 | 0 | 2 | 8 |
| [BE/project-service/src/main/java/com/kbase/project/model/MemberRole.java](/BE/project-service/src/main/java/com/kbase/project/model/MemberRole.java) | Java | 6 | 0 | 2 | 8 |
| [BE/project-service/src/main/java/com/kbase/project/model/Project.java](/BE/project-service/src/main/java/com/kbase/project/model/Project.java) | Java | 36 | 0 | 9 | 45 |
| [BE/project-service/src/main/java/com/kbase/project/model/ProjectInvitation.java](/BE/project-service/src/main/java/com/kbase/project/model/ProjectInvitation.java) | Java | 53 | 0 | 16 | 69 |
| [BE/project-service/src/main/java/com/kbase/project/model/ProjectMember.java](/BE/project-service/src/main/java/com/kbase/project/model/ProjectMember.java) | Java | 39 | 0 | 11 | 50 |
| [BE/project-service/src/main/java/com/kbase/project/model/ProjectMemberId.java](/BE/project-service/src/main/java/com/kbase/project/model/ProjectMemberId.java) | Java | 21 | 0 | 4 | 25 |
| [BE/project-service/src/main/java/com/kbase/project/repository/ProjectInvitationRepository.java](/BE/project-service/src/main/java/com/kbase/project/repository/ProjectInvitationRepository.java) | Java | 11 | 0 | 3 | 14 |
| [BE/project-service/src/main/java/com/kbase/project/repository/ProjectMemberRepository.java](/BE/project-service/src/main/java/com/kbase/project/repository/ProjectMemberRepository.java) | Java | 9 | 0 | 3 | 12 |
| [BE/project-service/src/main/java/com/kbase/project/repository/ProjectRepository.java](/BE/project-service/src/main/java/com/kbase/project/repository/ProjectRepository.java) | Java | 7 | 0 | 3 | 10 |
| [BE/project-service/src/main/java/com/kbase/project/service/EmailService.java](/BE/project-service/src/main/java/com/kbase/project/service/EmailService.java) | Java | 57 | 0 | 10 | 67 |
| [BE/project-service/src/main/java/com/kbase/project/service/ProjectService.java](/BE/project-service/src/main/java/com/kbase/project/service/ProjectService.java) | Java | 261 | 0 | 26 | 287 |
| [BE/project-service/src/main/resources/application.properties](/BE/project-service/src/main/resources/application.properties) | Java Properties | 20 | 0 | 6 | 26 |
| [BE/user-service/Dockerfile](/BE/user-service/Dockerfile) | Docker | 10 | 0 | 2 | 12 |
| [BE/user-service/pom.xml](/BE/user-service/pom.xml) | XML | 43 | 0 | 1 | 44 |
| [BE/user-service/src/main/java/com/kbase/user/UserServiceApplication.java](/BE/user-service/src/main/java/com/kbase/user/UserServiceApplication.java) | Java | 11 | 0 | 3 | 14 |
| [BE/user-service/src/main/java/com/kbase/user/common/ResponseTemplate.java](/BE/user-service/src/main/java/com/kbase/user/common/ResponseTemplate.java) | Java | 10 | 0 | 4 | 14 |
| [BE/user-service/src/main/java/com/kbase/user/config/SecurityConfig.java](/BE/user-service/src/main/java/com/kbase/user/config/SecurityConfig.java) | Java | 20 | 0 | 4 | 24 |
| [BE/user-service/src/main/java/com/kbase/user/controller/AuthController.java](/BE/user-service/src/main/java/com/kbase/user/controller/AuthController.java) | Java | 28 | 0 | 6 | 34 |
| [BE/user-service/src/main/java/com/kbase/user/controller/UserController.java](/BE/user-service/src/main/java/com/kbase/user/controller/UserController.java) | Java | 39 | 0 | 8 | 47 |
| [BE/user-service/src/main/java/com/kbase/user/dto/AuthResponse.java](/BE/user-service/src/main/java/com/kbase/user/dto/AuthResponse.java) | Java | 3 | 0 | 2 | 5 |
| [BE/user-service/src/main/java/com/kbase/user/dto/LoginRequest.java](/BE/user-service/src/main/java/com/kbase/user/dto/LoginRequest.java) | Java | 5 | 0 | 3 | 8 |
| [BE/user-service/src/main/java/com/kbase/user/dto/RegisterRequest.java](/BE/user-service/src/main/java/com/kbase/user/dto/RegisterRequest.java) | Java | 13 | 0 | 3 | 16 |
| [BE/user-service/src/main/java/com/kbase/user/dto/UpdateProfileRequest.java](/BE/user-service/src/main/java/com/kbase/user/dto/UpdateProfileRequest.java) | Java | 5 | 0 | 3 | 8 |
| [BE/user-service/src/main/java/com/kbase/user/dto/UserResponse.java](/BE/user-service/src/main/java/com/kbase/user/dto/UserResponse.java) | Java | 4 | 0 | 3 | 7 |
| [BE/user-service/src/main/java/com/kbase/user/exception/BadRequestException.java](/BE/user-service/src/main/java/com/kbase/user/exception/BadRequestException.java) | Java | 6 | 0 | 2 | 8 |
| [BE/user-service/src/main/java/com/kbase/user/exception/GlobalExceptionHandler.java](/BE/user-service/src/main/java/com/kbase/user/exception/GlobalExceptionHandler.java) | Java | 32 | 0 | 6 | 38 |
| [BE/user-service/src/main/java/com/kbase/user/exception/ResourceNotFoundException.java](/BE/user-service/src/main/java/com/kbase/user/exception/ResourceNotFoundException.java) | Java | 6 | 0 | 2 | 8 |
| [BE/user-service/src/main/java/com/kbase/user/model/User.java](/BE/user-service/src/main/java/com/kbase/user/model/User.java) | Java | 46 | 0 | 11 | 57 |
| [BE/user-service/src/main/java/com/kbase/user/model/UserRole.java](/BE/user-service/src/main/java/com/kbase/user/model/UserRole.java) | Java | 6 | 0 | 2 | 8 |
| [BE/user-service/src/main/java/com/kbase/user/repository/UserRepository.java](/BE/user-service/src/main/java/com/kbase/user/repository/UserRepository.java) | Java | 8 | 0 | 3 | 11 |
| [BE/user-service/src/main/java/com/kbase/user/service/JwtService.java](/BE/user-service/src/main/java/com/kbase/user/service/JwtService.java) | Java | 31 | 0 | 5 | 36 |
| [BE/user-service/src/main/java/com/kbase/user/service/UserService.java](/BE/user-service/src/main/java/com/kbase/user/service/UserService.java) | Java | 74 | 0 | 10 | 84 |
| [BE/user-service/src/main/resources/application.properties](/BE/user-service/src/main/resources/application.properties) | Java Properties | 14 | 0 | 5 | 19 |
| [FE/kbase-frontend/Dockerfile](/FE/kbase-frontend/Dockerfile) | Docker | 16 | 1 | 3 | 20 |
| [FE/kbase-frontend/next-env.d.ts](/FE/kbase-frontend/next-env.d.ts) | TypeScript | 1 | 4 | 2 | 7 |
| [FE/kbase-frontend/next.config.mjs](/FE/kbase-frontend/next.config.mjs) | JavaScript | 4 | 1 | 2 | 7 |
| [FE/kbase-frontend/package-lock.json](/FE/kbase-frontend/package-lock.json) | JSON | 7,037 | 0 | 1 | 7,038 |
| [FE/kbase-frontend/package.json](/FE/kbase-frontend/package.json) | JSON | 34 | 0 | 1 | 35 |
| [FE/kbase-frontend/postcss.config.js](/FE/kbase-frontend/postcss.config.js) | JavaScript | 6 | 0 | 1 | 7 |
| [FE/kbase-frontend/src/app/globals.css](/FE/kbase-frontend/src/app/globals.css) | PostCSS | 17 | 0 | 4 | 21 |
| [FE/kbase-frontend/src/app/invitations/accept/page.tsx](/FE/kbase-frontend/src/app/invitations/accept/page.tsx) | TypeScript JSX | 70 | 0 | 10 | 80 |
| [FE/kbase-frontend/src/app/layout.tsx](/FE/kbase-frontend/src/app/layout.tsx) | TypeScript JSX | 13 | 0 | 2 | 15 |
| [FE/kbase-frontend/src/app/page.tsx](/FE/kbase-frontend/src/app/page.tsx) | TypeScript JSX | 4 | 0 | 2 | 6 |
| [FE/kbase-frontend/src/components/HomeShell.tsx](/FE/kbase-frontend/src/components/HomeShell.tsx) | TypeScript JSX | 93 | 0 | 9 | 102 |
| [FE/kbase-frontend/src/components/Providers.tsx](/FE/kbase-frontend/src/components/Providers.tsx) | TypeScript JSX | 37 | 0 | 3 | 40 |
| [FE/kbase-frontend/src/components/auth/LoginForm.tsx](/FE/kbase-frontend/src/components/auth/LoginForm.tsx) | TypeScript JSX | 85 | 4 | 9 | 98 |
| [FE/kbase-frontend/src/components/auth/RegisterForm.tsx](/FE/kbase-frontend/src/components/auth/RegisterForm.tsx) | TypeScript JSX | 121 | 16 | 12 | 149 |
| [FE/kbase-frontend/src/components/projects/DocumentUpload.tsx](/FE/kbase-frontend/src/components/projects/DocumentUpload.tsx) | TypeScript JSX | 260 | 0 | 21 | 281 |
| [FE/kbase-frontend/src/components/projects/ProjectDashboard.tsx](/FE/kbase-frontend/src/components/projects/ProjectDashboard.tsx) | TypeScript JSX | 134 | 9 | 16 | 159 |
| [FE/kbase-frontend/src/components/projects/ProjectDetail.tsx](/FE/kbase-frontend/src/components/projects/ProjectDetail.tsx) | TypeScript JSX | 45 | 0 | 6 | 51 |
| [FE/kbase-frontend/src/components/projects/ProjectMembers.tsx](/FE/kbase-frontend/src/components/projects/ProjectMembers.tsx) | TypeScript JSX | 177 | 0 | 16 | 193 |
| [FE/kbase-frontend/src/lib/api.ts](/FE/kbase-frontend/src/lib/api.ts) | TypeScript | 23 | 0 | 4 | 27 |
| [FE/kbase-frontend/src/lib/types.ts](/FE/kbase-frontend/src/lib/types.ts) | TypeScript | 65 | 0 | 7 | 72 |
| [FE/kbase-frontend/src/store/authSlice.ts](/FE/kbase-frontend/src/store/authSlice.ts) | TypeScript | 77 | 0 | 9 | 86 |
| [FE/kbase-frontend/src/store/hooks.ts](/FE/kbase-frontend/src/store/hooks.ts) | TypeScript | 4 | 0 | 2 | 6 |
| [FE/kbase-frontend/src/store/projectSlice.ts](/FE/kbase-frontend/src/store/projectSlice.ts) | TypeScript | 50 | 0 | 7 | 57 |
| [FE/kbase-frontend/src/store/store.ts](/FE/kbase-frontend/src/store/store.ts) | TypeScript | 11 | 0 | 3 | 14 |
| [FE/kbase-frontend/tailwind.config.ts](/FE/kbase-frontend/tailwind.config.ts) | TypeScript | 17 | 0 | 3 | 20 |
| [FE/kbase-frontend/tsconfig.json](/FE/kbase-frontend/tsconfig.json) | JSON with Comments | 41 | 0 | 1 | 42 |
| [README.md](/README.md) | Markdown | 30 | 0 | 10 | 40 |
| [TRAINING/TRAINING/BackEnd.md](/TRAINING/TRAINING/BackEnd.md) | Markdown | 342 | 0 | 89 | 431 |
| [TRAINING/TRAINING/Database.md](/TRAINING/TRAINING/Database.md) | Markdown | 180 | 0 | 31 | 211 |
| [TRAINING/TRAINING/FrontEnd.md](/TRAINING/TRAINING/FrontEnd.md) | Markdown | 333 | 0 | 71 | 404 |
| [TRAINING/TRAINING/Training.md](/TRAINING/TRAINING/Training.md) | Markdown | 87 | 0 | 33 | 120 |
| [database/init/00-create-databases.sql](/database/init/00-create-databases.sql) | MS SQL | 3 | 0 | 1 | 4 |
| [database/init/01-user-schema.sql](/database/init/01-user-schema.sql) | MS SQL | 13 | 0 | 3 | 16 |
| [database/init/02-project-schema.sql](/database/init/02-project-schema.sql) | MS SQL | 50 | 0 | 6 | 56 |
| [database/init/03-document-schema.sql](/database/init/03-document-schema.sql) | MS SQL | 18 | 0 | 3 | 21 |
| [database/migrations/20260527\_project\_member\_invitations.sql](/database/migrations/20260527_project_member_invitations.sql) | MS SQL | 34 | 0 | 5 | 39 |
| [desktop.ini](/desktop.ini) | Ini | 2 | 0 | 1 | 3 |
| [docker-compose.yml](/docker-compose.yml) | YAML | 144 | 131 | 21 | 296 |

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)