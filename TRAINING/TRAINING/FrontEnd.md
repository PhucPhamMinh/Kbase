# KBase - FrontEnd

## 🎯 Overview

Web application để quản lý projects và documents, built với React/Next.js và Redux Toolkit.

---

## 🛠️ Tech Stack

### **Core Technologies**
- **Node.js 22** - Runtime environment
- **Next.js** - React framework với App Router
- **TypeScript** - Type safety
- **React** - UI library
- **Redux Toolkit** - State management
- **Axios** - HTTP client
- **Tailwind CSS** - Styling

### **Form & Validation**
- **React Hook Form** - Form management
- **Yup** - Schema validation
- **@hookform/resolvers** - Form resolver

### **UI/UX**
- **React Hot Toast** - Notifications
- **React Icons** - Icon library
- **date-fns** - Date formatting

---

## 📁 Project Structure

```
src/
├── app/                      # Next.js App Router
│   ├── (auth)/              # Auth pages group
│   │   ├── login/
│   │   └── register/
│   ├── (dashboard)/         # Protected pages group
│   │   ├── layout.tsx       # Dashboard layout với header/nav
│   │   ├── projects/        # Projects management
│   │   ├── users/           # User management (Admin)
│   │   └── documents/       # Documents browser
│   └── layout.tsx           # Root layout với Redux Provider
│
├── components/              # Reusable components
│   ├── layout/              # Layout components
│   ├── forms/               # Form components
│   ├── cards/               # Card components
│   └── common/              # Common UI components
│
├── lib/                     # Configurations
│   ├── axios.ts            # Axios instance với interceptors
│   ├── constants.ts        # App constants
│   └── utils.ts            # Utility functions
│
├── store/                   # Redux store
│   ├── index.ts            # Store configuration
│   ├── hooks.ts            # Typed Redux hooks
│   └── slices/             # Redux slices
│       ├── authSlice.ts
│       ├── projectSlice.ts
│       └── userSlice.ts
│
├── services/               # API services
│   ├── authService.ts      # Authentication APIs
│   ├── projectService.ts   # Project APIs
│   ├── userService.ts      # User APIs
│   └── documentService.ts  # Document APIs
│
└── types/                  # TypeScript types
    ├── auth.types.ts
    ├── project.types.ts
    ├── user.types.ts
    └── document.types.ts
```

---

## 📄 Pages & Features

### **1. Authentication Pages**

#### **Login Page** (`/login`)
- Email/password form
- Form validation
- JWT token storage
- Redirect to projects sau khi login
- Link đến register page

#### **Register Page** (`/register`)
- Full name, email, password, confirm password
- Form validation (email format, password length, password match)
- Success notification
- Redirect to login sau khi register thành công

---

### **2. Dashboard Pages** (Protected)

#### **Projects List Page** (`/projects`)
**Features:**
- Display all projects (grid layout)
- Create new project button
- Project cards hiển thị:
  - Project name
  - Description
  - Owner name
  - Active status
- Actions: View detail, Delete
- Empty state khi chưa có projects

#### **Project Detail Page** (`/projects/[id]`)
**Features:**
- Project information section
- Document upload section:
  - File picker
  - Drag & drop support
  - Upload progress indicator
  - File type validation
- Documents list table:
  - Title, file type, size, uploader
  - Download document
  - Delete document
- Member management (optional):
  - Add members
  - Remove members
  - Member list

#### **Create Project Page** (`/projects/new`)
**Features:**
- Project name input
- Description textarea
- Form validation
- Create và redirect to project detail

#### **Users Management Page** (`/users`) - Admin Only
**Features:**
- Users list table
- User information:
  - Email, full name, role, status
- Actions:
  - View/Edit user
  - Activate/Deactivate user
  - Delete user
- Role management (Admin, Owner, User)
- Search và filter users

---

### **3. Layout Components**

#### **Dashboard Layout**
- **Header:**
  - Logo/App name
  - Navigation links (Projects, Users)
  - User profile
  - Logout button
- **Main Content Area**
- **Protected Route Logic** - Redirect to login nếu chưa authenticated

#### **Root Layout**
- Redux Provider wrapper
- Toast notifications provider
- Global styles
- Font configuration

---

## 🔐 Authentication Flow

### **JWT Token Management**
1. Login success → Store JWT token trong localStorage
2. Axios interceptor tự động attach token vào headers
3. 401 response → Clear token và redirect to login
4. Protected routes check authentication state

### **Redux Auth State**
```
authState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  loading: boolean
}
```

---

## 🔄 State Management (Redux)

### **Auth Slice**
- `setCredentials(user, token)` - Login success
- `logout()` - Clear user và token
- `setLoading(boolean)`

### **Project Slice**
- `setProjects(projects[])` - Load projects
- `setCurrentProject(project)` - Set active project
- `addProject(project)` - Add new project
- `updateProject(project)` - Update project
- `deleteProject(projectId)` - Remove project
- `setLoading(boolean)`

### **User Slice** (Admin)
- `setUsers(users[])`
- `addUser(user)`
- `updateUser(user)`
- `deleteUser(userId)`
- `setLoading(boolean)`

---

## 🌐 API Integration

### **Axios Configuration**
- Base URL từ environment variables
- Request interceptor: Add JWT token
- Response interceptor: Handle 401, token refresh
- Timeout configuration
- Error handling

### **API Services**

#### **Auth Service**
- `login(email, password)` → `{ token, user }`
- `register(email, password, fullName)` → `{ token, user }`
- `logout()` → void
- `getCurrentUser()` → User

#### **Project Service**
- `getAllProjects()` → Project[]
- `getProjectById(id)` → Project
- `createProject(data)` → Project
- `updateProject(id, data)` → Project
- `deleteProject(id)` → void
- `getProjectMembers(projectId)` → Member[]
- `addProjectMember(projectId, userId)` → Member
- `removeProjectMember(projectId, userId)` → void

#### **Document Service**
- `getProjectDocuments(projectId)` → Document[]
- `uploadDocument(formData)` → Document
- `downloadDocument(documentId)` → Blob
- `deleteDocument(documentId)` → void

#### **User Service** (Admin)
- `getAllUsers()` → User[]
- `getUserById(id)` → User
- `updateUser(id, data)` → User
- `deleteUser(id)` → void
- `activateUser(id)` → void
- `deactivateUser(id)` → void

---

## 🎨 UI/UX Components

### **Reusable Components**
- **Button** - Primary, secondary, danger variants
- **Input** - Text, email, password, file
- **Modal** - Confirmation dialogs, forms
- **Loading** - Spinners, skeletons
- **Card** - Project card, user card, document card
- **Table** - Data tables với sorting
- **Toast** - Success, error, info notifications

### **Form Components**
- **LoginForm** - Email/password với validation
- **RegisterForm** - Full registration form
- **ProjectForm** - Create/edit project
- **UserForm** - Create/edit user (Admin)
- **FileUpload** - Drag & drop file upload

---

## 🚀 Development Workflow

### **Setup**
1. Install Node.js 22+ và npm
2. Create Next.js project với TypeScript
3. Install dependencies (Redux, Axios, Tailwind, etc.)
4. Setup folder structure
5. Configure environment variables

### **Development**
1. Create TypeScript types
2. Setup Redux store và slices
3. Create API services với Axios
4. Build pages theo features
5. Create reusable components
6. Add form validation
7. Implement error handling
8. Add loading states
9. Style với Tailwind CSS
10. Test all features

### **Testing**
- Manual testing trên browser
- Test authentication flow
- Test CRUD operations
- Test file upload/download
- Test responsive design
- Test error handling

---

## 📦 Build & Deployment

### **Build Production**
```bash
npm run build
```

### **Deployment Options**
- **Vercel** - Recommended cho Next.js
- **Netlify** - Alternative static hosting
- **Docker** - Containerized deployment
- **AWS/Azure/GCP** - Cloud platforms

### **Environment Variables**
```env
NEXT_PUBLIC_API_URL=https://api.kbase.com/api
```

---

## ✅ Features Checklist

### **Core Features**
- ✅ User authentication (Login/Register)
- ✅ JWT token management
- ✅ Protected routes
- ✅ Projects list và CRUD
- ✅ Project detail page
- ✅ Document upload/download
- ✅ User management (Admin)

### **Advanced Features** (Optional)
- ⬜ Project member management
- ⬜ Advanced search và filters
- ⬜ Pagination
- ⬜ Real-time notifications
- ⬜ Document preview
- ⬜ Drag & drop file upload
- ⬜ Dark mode
- ⬜ Multi-language support
- ⬜ Export data (CSV, PDF)
- ⬜ Activity logs

### **Performance**
- ⬜ Lazy loading components
- ⬜ Image optimization
- ⬜ Code splitting
- ⬜ Caching strategies
- ⬜ Service worker
- ⬜ Progressive Web App (PWA)

---

## 🎯 Learning Path

### **Beginner Level**
1. Setup project và dependencies
2. Create basic pages (Login, Register)
3. Implement authentication flow
4. Create projects list page
5. Basic CRUD operations

### **Intermediate Level**
1. Redux state management
2. File upload/download
3. Form validation
4. Error handling
5. Loading states
6. Responsive design

### **Advanced Level**
1. Advanced search và filters
2. Real-time features
3. Performance optimization
4. Testing (Jest, React Testing Library)
5. CI/CD pipeline
6. Docker containerization

---

## 📚 Resources

### **Documentation**
- Next.js: https://nextjs.org/docs
- React: https://react.dev
- Redux Toolkit: https://redux-toolkit.js.org
- Tailwind CSS: https://tailwindcss.com
- Axios: https://axios-http.com

### **Tools**
- **VS Code** - IDE
- **React DevTools** - Debug React
- **Redux DevTools** - Debug Redux
- **Postman** - API testing
- **Figma** - UI design
