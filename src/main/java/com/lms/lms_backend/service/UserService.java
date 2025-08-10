package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.UserRegistrationRequest;
import com.lms.lms_backend.model.User;
import com.lms.lms_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private final Logger logger = Logger.getLogger(UserService.class.getName());
    public String registerUser(UserRegistrationRequest userRegistrationRequest) {

        // Check if the user already exists
        Optional<User> existingUser = userRepository.findByEmail(userRegistrationRequest.getEmail());
        if (existingUser.isPresent() && existingUser.get().getEmail().equals(userRegistrationRequest.getEmail())) {
            logger.warning("User already exists: {} "+ userRegistrationRequest.getEmail());
            return "User already exists";
        }

        User user = new User();
        user.setFirstName(userRegistrationRequest.getFirstName());
        user.setLastName(userRegistrationRequest.getLastName());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setPassword(userRegistrationRequest.getPassword());
        user.setRole(userRegistrationRequest.getRole());
        userRepository.save(user);
        logger.info("User registered: {} "+ userRegistrationRequest.getEmail());
        return "User registered successfully";
    }

}
