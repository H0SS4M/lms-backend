# LMS Backend Project

A Spring Boot-based Learning Management System (LMS) backend with user management, course management, and enrollment functionality.

## Features Implemented

- ✅ User Management (CRUD operations)
- ✅ Course Management (Entity created) -> TODO
- ✅ Enrollment Management (Entity created) -> TODO
- ✅ Database migrations with Flyway
- ✅ Spring Security
- ✅ Global exception handling
- ✅ Validation with Bean Validation
- ✅ Pagination and search functionality
- ✅ JPA Auditing for timestamps

## Prerequisites

- Java 17
- Maven 3.6+
- PostgreSQL 12+

## Database Setup

1. Create PostgreSQL database:
```sql
CREATE DATABASE lms;
```

2. Update database configuration in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/lms
    username: postgres
    password: password
```

## Running the Application

1. Start the application:
```bash
mvn spring-boot:run
```

2. The application will start on `http://localhost:8080`

3. Flyway will automatically create the database schema with the following tables:
   - `users` - User management
   - `courses` - Course management  
   - `enrollments` - Enrollment management

## API Endpoints

### User Management

#### Register a new user
```bash
POST /api/users/register
Content-Type: application/json

{
  "email": "john.instructor@lms.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "INSTRUCTOR"
}
```

#### Get user by ID
```bash
GET /api/users/{id}
```

#### Update user
```bash
PUT /api/users/{id}
Content-Type: application/json

{
  "firstName": "John Updated",
  "lastName": "Doe Updated"
}
```

#### Get all users with pagination
```bash
GET /api/users?page=0&size=10&sortBy=createdAt&sortDirection=DESC
```

#### Search users
```bash
GET /api/users/search?term=john&page=0&size=10
```

#### Deactivate user
```bash
DELETE /api/users/{id}
```

## User Roles

- `ADMIN` - System administrator
- `INSTRUCTOR` - Course instructor
- `STUDENT` - Student user

## Project Structure

```
src/main/java/com/lms/lms_backend/
├── config/
│   ├── JpaConfig.java
│   └── SecurityConfig.java
├── controller/
│   └── UserContoller.java
├── dto/
│   ├── UserRegistrationRequest.java
│   ├── UserUpdateRequest.java
│   └── UserResponse.java
├── enums/
│   ├── UserRole.java
│   ├── CourseStatus.java
│   └── EnrollmentStatus.java
├── exception/
│   ├── BusinessException.java
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
├── model/
│   ├── BaseEntitiy.java
│   ├── User.java
│   ├── Course.java
│   └── Enrollment.java
├── repository/
│   └── UserRepository.java
├── service/
│   ├── UserService.java
│   └── impl/
│       └── UserServiceImpl.java
└── LmsBackendApplication.java
```

## Testing the API

### 1. Register a new user
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.instructor@lms.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "INSTRUCTOR"
  }'
```

### 2. Get user by ID
```bash
curl http://localhost:8080/api/users/1
```

### 3. Update user
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John Updated",
    "lastName": "Doe Updated"
  }'
```

### 4. Get all users
```bash
curl "http://localhost:8080/api/users?page=0&size=5&sortBy=createdAt&sortDirection=DESC"
```

### 5. Search users
```bash
curl "http://localhost:8080/api/users/search?term=john&page=0&size=10"
```

## Next Steps

The following features are ready for implementation in the next session:

1. **Course Management API** - CRUD operations for courses
2. **Enrollment Management API** - Student enrollment in courses
3. **JWT Authentication** - Proper authentication and authorization
4. **Role-based Access Control** - Different permissions for different roles
5. **File Upload** - Course materials and user avatars
6. **Email Notifications** - Welcome emails and course updates

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Ensure PostgreSQL is running
   - Check database credentials in `application.yml`
   - Verify database exists

2. **Flyway Migration Errors**
   - Check database permissions
   - Ensure no conflicting schema exists

3. **Compilation Errors**
   - Ensure Java 17 is installed
   - Run `mvn clean compile` to check for issues

## Development Notes

- The application uses Spring Boot 3.5.4
- JPA/Hibernate for database operations
- Flyway for database migrations
- Spring Security for authentication (temporarily disabled for testing)
- Lombok for reducing boilerplate code
- Bean Validation for input validation
