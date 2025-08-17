package com.lms.lms_backend.dto;

import com.lms.lms_backend.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private InstructorInfo instructor;
    private CourseStatus status;
    private Integer capacity;
    private Integer enrolledCount;
    private Integer availableSeats;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InstructorInfo {
        private Long id;
        private String name;
        private String email;
    }
}
