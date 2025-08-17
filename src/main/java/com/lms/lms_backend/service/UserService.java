package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.UserRegistrationRequest;
import com.lms.lms_backend.dto.UserUpdateRequest;
import com.lms.lms_backend.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest request);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    Page<UserResponse> getAllUsers(Pageable pageable);
    Page<UserResponse> searchUsers(String searchTerm, Pageable pageable);
    void deactivateUser(Long id);
}
