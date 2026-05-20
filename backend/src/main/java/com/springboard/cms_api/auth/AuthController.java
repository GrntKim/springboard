package com.springboard.cms_api.auth;

import com.springboard.cms_api.auth.dto.RegisterRequest;
import com.springboard.cms_api.user.UserService;
import com.springboard.cms_api.user.dto.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register User")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User Registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Login id already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @Valid @RequestBody RegisterRequest request
    ) {
        userService.createUser(new CreateUserRequest(
                request.loginId(),
                request.password(),
                request.nickname()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
