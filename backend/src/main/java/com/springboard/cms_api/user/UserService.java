package com.springboard.cms_api.user;

import com.springboard.cms_api.user.dto.CreateUserRequest;
import com.springboard.cms_api.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getUsers() { return userRepository.findAll(); }

    public UserResponse getUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userRepository.findById(id);
    }

    public void validateDuplicateUsername(String username) {
        if(userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }
    }

    public void createUser(CreateUserRequest request) {
        validateDuplicateUsername(request.username());

        userRepository.save(
                request.username(),
                passwordEncoder.encode(request.password()),
                request.displayName()
        );
    }

}
