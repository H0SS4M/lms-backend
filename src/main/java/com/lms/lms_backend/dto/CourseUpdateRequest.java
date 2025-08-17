package com.lms.lms_backend.dto;

import com.lms.lms_backend.enums.CourseStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateRequest {
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255)
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10)
    private String description;
    
    @Min(value = 1)
    @Max(value = 1000)
    private Integer capacity;
    
    private CourseStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
