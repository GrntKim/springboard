package com.springboard.cms_api.post;

import com.springboard.cms_api.post.dto.PostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/posts")
    public ResponseEntity<List<PostResponse>> getPosts() {
        List<PostResponse> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }
}
