package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.UserRegistrationRequest;
import com.lms.lms_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
//@RequiredArgsConstructor
public class UserContoller {
    @Autowired
    private final UserService userService;
//    private final UserService userService;
    public UserContoller(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    void registerUser(@Valid UserRegistrationRequest userRegistrationRequest) {
        userService.registerUser(userRegistrationRequest);
    }
}
