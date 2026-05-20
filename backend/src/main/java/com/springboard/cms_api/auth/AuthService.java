package com.springboard.cms_api.auth;

import com.springboard.cms_api.auth.dto.LoginRequest;
import com.springboard.cms_api.user.UserAccount;
import com.springboard.cms_api.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private static final String LOGIN_USER_ID = "LOGIN_USER_ID";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(@Valid LoginRequest request, HttpSession session) {
        UserAccount user = userRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid login id or password."
                ));

        if (!passwordEncoder.matches(request.password(), user.password())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid login id or password."
            );
        }
        session.setAttribute(LOGIN_USER_ID, user.id());
    }
}
