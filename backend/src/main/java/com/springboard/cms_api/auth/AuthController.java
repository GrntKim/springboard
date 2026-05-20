package com.springboard.cms_api.auth;

import com.springboard.cms_api.auth.dto.LoginRequest;
import com.springboard.cms_api.auth.dto.RegisterRequest;
import com.springboard.cms_api.user.UserService;
import com.springboard.cms_api.user.dto.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
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
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }


    @Operation(summary = "Register User")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User Registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Login id already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        userService.createUser(new CreateUserRequest(
                request.loginId(),
                request.password(),
                request.nickname()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Login")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Invalid login id or password")
    })
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        authService.login(request, session);
        return ResponseEntity.noContent().build();
    }
}
