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

    public List<PostResponse> getPosts() {
        return postRepository.findAll();
    }

    public PostResponse getPost(Long id) {
        if(!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        return postRepository.findById(id);
    }

    public void createPost(CreatePostRequest request) {
        if(!userRepository.existsById(request.userId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        postRepository.save(
                request.userId(),
                request.title(),
                request.content()
        );
    }

    public void updatePost(Long postId, @Valid UpdatePostRequest request) {
        if(!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        postRepository.update(
                postId,
                request.title(),
                request.content()
        );
    }

    public void deletePost(Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepository.delete(postId);
    }
}
