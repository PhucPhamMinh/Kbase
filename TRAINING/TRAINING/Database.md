# KBase - Database

---

## 1️⃣ DATABASE - POSTGRESQL

### 📥 **Step 1: PostgreSQL Installation**

#### **Method 1: Install from official website**
1. Visit: https://www.postgresql.org/download/
2. Select your OS (Windows/Mac/Linux)
3. Download and install (recommended version: 15 or 16)
4. Remember the password for `postgres` user
5. Install pgAdmin (database management tool)

#### **Method 2: Install via Docker (Recommended for learning)**
- Install Docker Desktop (if you have a license)
- Run command to install PostgreSQL

---

### 📖 **Step 2: Connect to Database**

Use command line or database tools (DBeaver, pgAdmin, TablePlus, ...) to connect to PostgreSQL

#### **2.3. Create Tables - KBase Project**

```sql
-- 1. USERS Table
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'OWNER', 'USER')),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. PROJECTS Table
CREATE TABLE projects (
    project_id SERIAL PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. PROJECT_MEMBERS Table (Using composite primary key)
CREATE TABLE project_members (
    project_id INTEGER NOT NULL REFERENCES projects(project_id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    role VARCHAR(50) DEFAULT 'MEMBER' CHECK (role IN ('OWNER', 'MEMBER')),
    is_active BOOLEAN DEFAULT true,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (project_id, user_id)  -- Composite primary key from 2 foreign keys
);

-- 4. DOCUMENTS Table
CREATE TABLE documents (
    document_id SERIAL PRIMARY KEY,
    project_id INTEGER NOT NULL REFERENCES projects(project_id) ON DELETE CASCADE,
    uploaded_by INTEGER NOT NULL REFERENCES users(user_id),
    title VARCHAR(255),
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_size BIGINT NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    mime_type VARCHAR(100),
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### 📊 **Entity Relationship Diagram**

```
┌─────────────────┐
│     USERS       │
│─────────────────│
│ PK: user_id     │
│     email       │
│     password    │
│     full_name   │
│     role        │
│     is_active   │
└─────────────────┘
         │
         │ 1
         │
         │ owns
         │
         │ N
         ▼
┌─────────────────┐           N ┌──────────────────┐ N
│    PROJECTS     │◄──────────────│ PROJECT_MEMBERS  │──────────────┐
│─────────────────│  belongs to   │──────────────────│              │
│ PK: project_id  │               │ PK: project_id,  │              │
│     name        │               │     user_id      │              │
│     description │               │     role         │              │
│ FK: owner_id    │               │     is_active    │              │
│     is_active   │               └──────────────────┘              │
└─────────────────┘                                                 │
         │                                                          │
         │ 1                                                        │ N
         │                                                          │
         │ has                                              joins   │
         │                                                          │
         │ N                                                        │ 1
         ▼                                                          ▼
┌─────────────────┐                                        ┌─────────────────┐
│   DOCUMENTS     │                                        │     USERS       │
│─────────────────│                                        │  (same table)   │
│ PK: document_id │                                        └─────────────────┘
│     title       │
│     file_name   │
│     file_type   │
│     file_size   │
│ FK: project_id  │
│ FK: uploaded_by │
│     is_active   │
└─────────────────┘

**Relationships:**
- 1 USER can own N PROJECTS (owner_id)
- 1 USER can be member of N PROJECTS (via PROJECT_MEMBERS)
- 1 PROJECT can have N MEMBERS (via PROJECT_MEMBERS)
- 1 PROJECT can have N DOCUMENTS
- 1 USER can upload N DOCUMENTS
```

---

### 🔧 **Step 3: SQL Commands to Learn**

**Table Management:**
- `CREATE TABLE` - Create new table
- `DROP TABLE` - Delete table
- `ALTER TABLE ADD COLUMN` - Add new column
- `ALTER TABLE DROP COLUMN` - Remove column
- `ALTER TABLE ALTER COLUMN` - Modify column properties (type, constraints, default value)
- `TRUNCATE TABLE` - Delete all data but keep table structure

**Data Operations (CRUD):**
- `INSERT INTO` - Add new records
- `SELECT` - Query/retrieve data
- `UPDATE` - Modify existing records
- `DELETE FROM` - Remove records

**Basic Query Commands:**
- `DISTINCT` - Get unique values (remove duplicates)
- `WHERE` - Filter rows with conditions
- `ORDER BY` - Sort results (ASC/DESC)
- `LIMIT` - Limit number of results
- `OFFSET` - Skip rows (pagination)
- `AS` - Alias for columns/tables
- `LIKE` - Pattern matching (%, _)
- `IN` - Match multiple values
- `BETWEEN` - Range condition
- `IS NULL` / `IS NOT NULL` - Check null values
- `AND`, `OR`, `NOT` - Logical operators

**Aggregate Functions:**
- `COUNT()` - Count rows
- `SUM()` - Sum values
- `AVG()` - Average value
- `MIN()` / `MAX()` - Minimum/Maximum value

**Advanced Queries:**
- `JOIN` (INNER, LEFT, RIGHT, FULL) - Combine tables
- `GROUP BY` - Group data for aggregation
- `HAVING` - Filter grouped data
- Subqueries - Nested queries

**Performance & Optimization:**
- `CREATE INDEX` - Create index for faster queries
- Index types - B-tree (default), Hash, GIN, GiST
- `DROP INDEX` - Remove index
- `EXPLAIN` - Show query execution plan
- `EXPLAIN ANALYZE` - Execute and show actual performance
- Query optimization techniques - Avoid SELECT *, use WHERE efficiently, limit result sets

---

### 📊 **Practice Exercises**

#### **Exercise 1: Basic**
1. Create 3 new users with different roles
2. Create 2 new projects
3. Add members to projects
4. Upload 5 documents to projects

#### **Exercise 2: Queries**
1. List all projects of user with email 'owner1@kbase.com'
2. Count documents by file_type
3. Find top 5 documents with largest file size
4. List users who haven't uploaded any documents

#### **Exercise 3: Advanced**
1. Find project with largest total file size
2. List users and number of projects they joined, sorted descending
3. Find documents uploaded in the last 7 days
4. Create a report: Each project with member count, document count, total storage
