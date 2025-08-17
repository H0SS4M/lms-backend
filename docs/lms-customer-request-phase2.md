### 2. Course Management System

**Business Context:** We have 50+ instructors ready to upload their courses. The Spring semester starts February 1st, and students need to enroll by January 25th.

#### 2.1 Course Entity Requirements
```
Course must include:
- Title (required, 3-255 characters)
- Description (required, minimum 10 characters) 
- Instructor (linked to user)
- Status (DRAFT, PUBLISHED, ARCHIVED)
- Capacity (1-1000 students)
- Enrolled count (auto-calculated)
- Start date (must be future date)
- End date (must be after start date)
```

#### 2.2 Course CRUD Operations

**Business Rules:**
- Only instructors and admins can create courses
- Instructors can only modify/delete their own courses
- Cannot delete courses with active enrollments
- Cannot reduce capacity below current enrollment
- Draft courses are not visible to students

**Required Endpoints:**
| Method | Endpoint | Access | Business Rule |
|--------|----------|--------|---------------|
| POST | /api/courses | Instructor/Admin | Auto-assign current user as instructor |
| GET | /api/courses | Public | Only show PUBLISHED courses |
| GET | /api/courses/{id} | Authenticated | Show course details |
| PUT | /api/courses/{id} | Course Owner/Admin | Cannot modify if course started |
| DELETE | /api/courses/{id} | Course Owner/Admin | Only if no enrollments |

#### 2.3 Course Search & Filter
Students need powerful search to find courses:
- Search by title or description (partial match)
- Filter by status
- Filter by instructor
- Show upcoming courses (starting soon)
- **Performance:** Results in under 1 second for 10,000 courses

---

### 3. Enrollment System

**Critical Business Requirement:** Registration opens January 25th. We expect 5,000 students enrolling in the first hour. The system MUST handle this load!

#### 3.1 Enrollment Rules
```
A student CAN:
- Enroll in multiple courses
- Drop a course before it starts
- View their enrollment status

A student CANNOT:
- Enroll in the same course twice
- Enroll after course starts
- Enroll in a full course
- Enroll in DRAFT courses
```

#### 3.2 Enrollment Endpoints

| Method | Endpoint | Purpose | Validation |
|--------|----------|---------|------------|
| POST | /api/enrollments | Student enrolls | Check capacity, status, dates |
| PUT | /api/enrollments/{id}/status | Update enrollment | Students can only DROP |
| GET | /api/enrollments/users/{userId}/courses | User's courses | Users see own, Admin sees all |
| GET | /api/enrollments/courses/{courseId}/students | Course students | Instructor/Admin only |

#### 3.3 Enrollment Statuses
- **PENDING:** Initial enrollment (awaiting payment - future feature)
- **ACTIVE:** Confirmed enrollment
- **COMPLETED:** Course finished successfully
- **DROPPED:** Student withdrew

**Business Logic:**
- When enrolled → increment course enrolled_count
- When dropped → decrement course enrolled_count
- Track enrollment date for reporting
- If completed, set progress to 100%

---

### 4. Advanced Analytics Requirements

**Management Dashboard needs real-time statistics:**

#### 4.1 Course Statistics
GET /api/courses/{id}/stats

Required metrics:
```json
{
  "totalEnrollments": 150,
  "activeEnrollments": 145,
  "completedEnrollments": 0,
  "droppedEnrollments": 5,
  "completionRate": 0.0,
  "dropRate": 3.33,
  "availableSeats": 50
}
```

#### 4.2 Instructor Dashboard
GET /api/dashboard/instructor

Show instructor's performance:
```json
{
  "totalCourses": 5,
  "publishedCourses": 3,
  "totalStudents": 450,
  "averageCompletionRate": 78.5,
  "recentCourses": [...]
}
```

---


## 📎 Test Data for Phase 2

### Sample Course Data
```json
{
  "title": "Introduction to Artificial Intelligence",
  "description": "Comprehensive introduction to AI concepts, including machine learning, neural networks, and practical applications",
  "capacity": 200,
  "status": "PUBLISHED",
  "startDate": "2025-02-01T09:00:00",
  "endDate": "2025-05-15T17:00:00"
}
```

### Expected Statistics Response
```json
{
  "courseId": 1,
  "courseTitle": "Introduction to AI",
  "totalEnrollments": 185,
  "activeEnrollments": 180,
  "completedEnrollments": 0,
  "droppedEnrollments": 5,
  "completionRate": 0.0,
  "dropRate": 2.7,
  "availableSeats": 15,
  "enrollmentsByStatus": {
    "ACTIVE": 180,
    "DROPPED": 5
  }
}
```