package com.springboard.cms_api.user;

import com.springboard.cms_api.user.dto.CreateUserRequest;
import com.springboard.cms_api.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
