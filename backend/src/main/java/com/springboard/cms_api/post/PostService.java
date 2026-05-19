package com.springboard.cms_api.post;

import com.springboard.cms_api.post.dto.CreatePostRequest;
import com.springboard.cms_api.post.dto.PostResponse;
import com.springboard.cms_api.post.dto.UpdatePostRequest;
import com.springboard.cms_api.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void validatePostIdExists(Long id) {
        if(!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }

    public void validateUserIdExists(Long id) {
        if(!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public List<PostResponse> getPosts() {
        return postRepository.findAll();
    }

    public List<PostResponse> getPostsByUserId(Long userId) {
        validateUserIdExists(userId);
        return postRepository.findAllByUserId(userId);
    }

    public PostResponse getPost(Long id) {
        validatePostIdExists(id);
        return postRepository.findById(id);
    }

    public void createPost(CreatePostRequest request) {
        validateUserIdExists(request.userId());
        postRepository.save(
                request.userId(),
                request.title(),
                request.content()
        );
    }

    public void updatePost(Long id, @Valid UpdatePostRequest request) {
        validatePostIdExists(id);
        postRepository.update(
                id,
                request.title(),
                request.content()
        );
    }

    public void deletePost(Long id) {
        validatePostIdExists(id);
        postRepository.delete(id);
    }

}
