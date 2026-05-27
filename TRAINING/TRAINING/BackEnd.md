# KBase - BackEnd

## 1️⃣ JAVA SPRING BOOT

### 📥 **Step 1: Development Environment Setup**

#### **1.1. Install OpenJDK**
1. Visit: https://adoptium.net/ (Eclipse Temurin)
2. Download OpenJDK 17 or 21 (LTS versions)
3. Install and set JAVA_HOME environment variable
4. Verify: `java -version`

#### **1.2. Install IDE**
Choose one of the following:
- **IntelliJ IDEA** (if you have a license) - Recommended
- **Eclipse** - Free, widely used
- **VS Code** with Java extensions - Lightweight

---

### 🚀 **Step 2: Create Spring Boot Project**

#### **2.1. Project Setup**
Use Spring Initializr: https://start.spring.io/
- **Project:** Maven
- **Language:** Java
- **Spring Boot:** 3.2.x or latest stable
- **Java Version:** 17 or 21
- **Packaging:** Jar

#### **2.2. Add Dependencies**
Required libraries:
- **Spring Web** - For REST API
- **Spring Data JPA** - Database ORM
- **PostgreSQL Driver** - Database connection
- **Lombok** - Reduce boilerplate code
- **Spring Boot DevTools** - Hot reload during development
- **Validation** - Input validation

Optional:
- **Spring Security** - Authentication & Authorization
- **SpringDoc OpenAPI** - API documentation (Swagger)

---

### 🧪 **Step 3: Create Test API**

#### **3.1. Create Ping Controller**
```java
@RestController
@RequestMapping("/api")
public class PingController {
    
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
```

**Test:** Run application and visit `http://localhost:8080/api/ping`

---

### 💾 **Step 4: Database Configuration**

#### **4.1. Configure application.properties**
```properties
# Database connection
spring.datasource.url=jdbc:postgresql://localhost:5432/kbase_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

### 🏗️ **Step 5: Create Entity Models**

Create entity classes matching the database design:
- **User** - User information
- **Project** - Project details
- **ProjectMember** - Project membership (composite key with project_id + user_id)
- **Document** - Document/file information

**Example - User Entity:**
```java
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String fullName;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    private Boolean isActive = true;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

Create similar entities for Project, ProjectMember, and Document based on the database schema.

---

### 🔌 **Step 6: Create Repository Interfaces**

Create repository interfaces for database operations:
- **UserRepository** - User data access
- **ProjectRepository** - Project data access
- **DocumentRepository** - Document data access

**Example - User Repository:**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> findByIsActive(Boolean isActive);
}
```

Create similar repositories for Project and Document.

---

### 🎯 **Step 7: Create REST APIs**

Create REST API controllers with CRUD operations:
- **UserController** - `/api/users` (GET, POST, PUT, DELETE)
- **ProjectController** - `/api/projects` (GET, POST, PUT, DELETE)
- **DocumentController** - `/api/documents` (GET, POST, PUT, DELETE)

**Example - User API:**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseTemplate<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseTemplate.success(users);
    }
    
    @GetMapping("/{id}")
    public ResponseTemplate<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseTemplate.success(user);
    }
    
    @PostMapping
    public ResponseTemplate<UserDTO> createUser(@RequestBody UserDTO user) {
        UserDTO created = userService.create(user);
        return ResponseTemplate.success("User created successfully", created);
    }
    
    @PutMapping("/{id}")
    public ResponseTemplate<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO user) {
        UserDTO updated = userService.update(id, user);
        return ResponseTemplate.success("User updated successfully", updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseTemplate<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseTemplate.success("User deleted successfully", null);
    }
}
```

Create similar controllers for Project and Document.

---

### 🏢 **Step 8: Create Service Layer**

Create service classes for business logic:
- **UserService** - User business logic
- **ProjectService** - Project business logic
- **DocumentService** - Document business logic

**Example - UserService:**
```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public List<UserDTO> findAll() {
        // Convert Entity to DTO
        return userRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDTO(user);
    }
    
    public UserDTO create(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return convertToDTO(saved);
    }
    
    public UserDTO update(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setRole(userDTO.getRole());
        User updated = userRepository.save(existingUser);
        return convertToDTO(updated);
    }
    
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    
    // Helper methods
    private UserDTO convertToDTO(User user) {
        // Use ModelMapper or manual mapping
    }
    
    private User convertToEntity(UserDTO dto) {
        // Use ModelMapper or manual mapping
    }
}
```

Create similar service classes for Project and Document.

---

### 📝 **Step 9: Add Response Template & DTOs**

#### **9.1. Create Generic Response Template**

Create a standardized response format for all APIs:

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplate<T> {
    private int status;           // HTTP status code
    private String message;       // Response message
    private T result;             // Actual data
    private Object metadata;      // Additional info (pagination, etc.)
}
```

**Usage in Controller:**
```java
@GetMapping
public ResponseTemplate<List<UserDTO>> getAllUsers() {
    List<UserDTO> users = userService.findAll();
    return ResponseTemplate.success(users);
}

@PostMapping
public ResponseTemplate<UserDTO> createUser(@RequestBody UserDTO user) {
    UserDTO created = userService.create(user);
    return ResponseTemplate.success("User created successfully", created);
}
```

#### **9.2. Create DTO Classes**

Create DTO classes to separate API layer from database entities:

```java
// User DTO
@Data
public class UserDTO {
    private Long userId;
    private String email;
    private String fullName;
    private UserRole role;
    private Boolean isActive;
}

// Project DTO
@Data
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private String description;
    private Long ownerId;
    private String ownerName;
    private Boolean isActive;
}

// Document DTO
@Data
public class DocumentDTO {
    private Long documentId;
    private String title;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Long projectId;
    private Long uploadedBy;
}
```

---

### 🛡️ **Step 10: Exception Handling**

Create global exception handler:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---

### ✅ **Step 11: Testing APIs**

Use tools to test your APIs:
- **Postman** - Popular API testing tool
- **Swagger UI** - If using SpringDoc OpenAPI

**Test endpoints:**
- `GET http://localhost:8080/api/ping`
- `GET http://localhost:8080/api/users`
- `POST http://localhost:8080/api/users`
- `GET http://localhost:8080/api/projects`
- `POST http://localhost:8080/api/documents`

---

### 🎯 **Practice Exercises**

#### **Exercise 1: Basic Setup**
1. Set up development environment (Java, IDE)
2. Create Spring Boot project with required dependencies
3. Create /api/ping endpoint and test it
4. Configure PostgreSQL connection

#### **Exercise 2: Entities & Database**
1. Create all entity classes (User, Project, ProjectMember, Document)
2. Run application and verify tables are created
3. Create repository interfaces
4. Test database connection

#### **Exercise 3: REST APIs**
1. Create User CRUD APIs
2. Create Project CRUD APIs
3. Create Document CRUD APIs
4. Test all endpoints

#### **Exercise 4: Advanced**
1. Add validation (@Valid, @NotNull, @Email)
2. Create DTO classes and use ModelMapper
3. Implement global exception handling
4. Add pagination to GET endpoints
5. Create custom query methods in repositories

#### **Exercise 5: Swagger UI**
1. Add SpringDoc OpenAPI dependency
2. Configure Swagger with API title and description
3. Add API documentation annotations (@Operation, @ApiResponse)
4. Test all endpoints using Swagger UI
5. Customize Swagger UI appearance

#### **Exercise 6: Security**
1. Add authentication endpoints (register, login, logout)
2. Implement password hashing with BCrypt
3. Create JWT token generation and validation (optional)
4. Add request/response DTOs for authentication
5. Test authentication flow

#### **Exercise 7: Deployment**
1. Create Dockerfile for Spring Boot application
2. Create docker-compose.yml with PostgreSQL and app
3. Build Docker image
4. Run application in Docker container
5. Test APIs in containerized environment
