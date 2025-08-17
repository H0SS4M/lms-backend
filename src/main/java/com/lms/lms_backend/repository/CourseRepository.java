package com.lms.lms_backend.repository;

import com.lms.lms_backend.enums.CourseStatus;
import com.lms.lms_backend.model.Course;
import com.lms.lms_backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
    
    Page<Course> findByInstructor(User instructor, Pageable pageable);
    
    Page<Course> findByInstructorId(Long instructorId, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.status = :status AND c.startDate > :date")
    Page<Course> findUpcomingCourses(@Param("status") CourseStatus status, 
                                      @Param("date") LocalDateTime date, 
                                      Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE " +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Course> searchCourses(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Course c WHERE c.instructor.id = :instructorId")
    Long countByInstructorId(@Param("instructorId") Long instructorId);
    
    boolean existsByIdAndInstructorId(Long courseId, Long instructorId);
}
