package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.MessageResponse;
import com.openclassrooms.mddapi.dto.UpdateUserRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            log.info("Getting current user profile");
            UserResponse response = userService.getCurrentUser();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error getting current user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
        try {
            log.info("Updating current user profile");
            UserResponse response = userService.updateCurrentUser(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating current user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}




