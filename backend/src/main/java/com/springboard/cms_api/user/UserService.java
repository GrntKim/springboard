package com.springboard.cms_api.user;

import com.springboard.cms_api.user.dto.CreateUserRequest;
import com.springboard.cms_api.user.dto.UpdateUserRequest;
import com.springboard.cms_api.user.dto.UserResponse;
import jakarta.validation.Valid;
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

    public void validateUserIdExists(Long id) {
        if(!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public void validateDuplicateUsername(String username) {
        if(userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }
    }

    public List<UserResponse> getUsers() { return userRepository.findAll(); }

    public UserResponse getUser(Long id) {
        validateUserIdExists(id);
        return userRepository.findById(id);
    }


    public void createUser(CreateUserRequest request) {
        validateDuplicateUsername(request.username());

        userRepository.save(
                request.username(),
                passwordEncoder.encode(request.password()),
                request.displayName()
        );
    }

    public void updateUser(Long id, @Valid UpdateUserRequest request) {
        validateUserIdExists(id);
        String encodedPassword = passwordEncoder.encode(request.password());
        validateDuplicateUsername(request.username());
        userRepository.update(
                id,
                request.username(),
                encodedPassword,
                request.displayName()
        );
    }

    public void deleteUser(Long id) {
        validateUserIdExists(id);
        userRepository.delete(id);
    }
}
