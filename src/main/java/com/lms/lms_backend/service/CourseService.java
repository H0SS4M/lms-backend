package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.CourseCreateRequest;
import com.lms.lms_backend.dto.CourseUpdateRequest;
import com.lms.lms_backend.dto.CourseResponse;
import com.lms.lms_backend.model.Course;
import com.lms.lms_backend.model.User;
import com.lms.lms_backend.enums.CourseStatus;
import com.lms.lms_backend.enums.UserRole;
import com.lms.lms_backend.exception.BusinessException;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseResponse createCourse(CourseCreateRequest request) {
        CustomUserDetails currentUser = getCurrentUser();

        if (currentUser.getRole() != UserRole.INSTRUCTOR && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only instructors can create courses");
        }

        validateCourseDates(request.getStartDate(), request.getEndDate());

        User instructor = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .instructor(instructor)
                .status(request.getStatus())
                .capacity(request.getCapacity())
                .enrolledCount(0)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Course savedCourse = courseRepository.save(course);
        log.info("Course created with id: {} by instructor: {}", savedCourse.getId(), instructor.getId());

        return mapToResponse(savedCourse);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return mapToResponse(course);
    }

    public CourseResponse updateCourse(Long id, CourseUpdateRequest request) {
        CustomUserDetails currentUser = getCurrentUser();

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        // Check if user is the course instructor or admin
        if (!course.getInstructor().getId().equals(currentUser.getId()) &&
                currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("You can only update your own courses");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            validateCourseDates(request.getStartDate(), request.getEndDate());
        }

        // Check if reducing capacity below enrolled count
        if (request.getCapacity() != null && request.getCapacity() < course.getEnrolledCount()) {
            throw new BusinessException("Cannot reduce capacity below current enrollment count");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        if (request.getCapacity() != null) {
            course.setCapacity(request.getCapacity());
        }
        if (request.getStatus() != null) {
            course.setStatus(request.getStatus());
        }
        if (request.getStartDate() != null) {
            course.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            course.setEndDate(request.getEndDate());
        }

        Course updatedCourse = courseRepository.save(course);
        log.info("Course updated with id: {}", updatedCourse.getId());

        return mapToResponse(updatedCourse);
    }

    public void deleteCourse(Long id) {
        CustomUserDetails currentUser = getCurrentUser();

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        if (!course.getInstructor().getId().equals(currentUser.getId()) &&
                currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("You can only delete your own courses");
        }

        if (course.getEnrolledCount() > 0) {
            throw new BusinessException("Cannot delete course with active enrollments");
        }

        courseRepository.delete(course);
        log.info("Course deleted with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> searchCourses(String searchTerm, CourseStatus status, Pageable pageable) {
        Page<Course> courses;

        if (status != null) {
            courses = courseRepository.findByStatus(status, pageable);
        } else if (searchTerm != null && !searchTerm.isEmpty()) {
            courses = courseRepository.searchCourses(searchTerm, pageable);
        } else {
            courses = courseRepository.findAll(pageable);
        }

        return courses.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> getInstructorCourses(Long instructorId, Pageable pageable) {
        return courseRepository.findByInstructorId(instructorId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> getUpcomingCourses(Pageable pageable) {
        return courseRepository.findUpcomingCourses(CourseStatus.PUBLISHED, LocalDateTime.now(), pageable)
                .map(this::mapToResponse);
    }

    private void validateCourseDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new BusinessException("Start date must be before end date");
        }
        if (startDate.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Start date must be in the future");
        }
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .instructor(CourseResponse.InstructorInfo.builder()
                        .id(course.getInstructor().getId())
                        .name(course.getInstructor().getFullName())
                        .email(course.getInstructor().getEmail())
                        .build())
                .status(course.getStatus())
                .capacity(course.getCapacity())
                .enrolledCount(course.getEnrolledCount())
                .availableSeats(course.getCapacity() - course.getEnrolledCount())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
