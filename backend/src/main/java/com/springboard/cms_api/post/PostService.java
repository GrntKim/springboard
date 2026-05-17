package com.springboard.cms_api.post;

import com.springboard.cms_api.post.dto.CreatePostRequest;
import com.springboard.cms_api.post.dto.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostResponse> getPosts() {
        return postRepository.findAll();
    }

    public PostResponse getPost(Long id) { return postRepository.findById(id); }

    public void createPost(CreatePostRequest request) {
        postRepository.save(
                request.userId(),
                request.title(),
                request.content()
        );
    }
}
