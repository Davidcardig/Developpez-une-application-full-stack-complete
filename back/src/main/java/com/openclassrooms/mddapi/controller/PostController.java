package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    private final PostService postService;

    /** GET /api/posts/feed — Fil d'actualité de l'utilisateur connecté */
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getFeed() {
        log.info("Fetching feed for current user");
        return ResponseEntity.ok(postService.getFeed());
    }

    /** GET /api/posts/{id} — Détail d'un article avec ses commentaires */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(postService.getPost(id));
        } catch (RuntimeException e) {
            log.error("Error fetching post {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /** POST /api/posts — Créer un article */
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest request) {
        try {
            log.info("Creating post with theme id: {}", request.getThemeId());
            return ResponseEntity.ok(postService.createPost(request));
        } catch (RuntimeException e) {
            log.error("Error creating post: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /** POST /api/posts/{id}/comments — Ajouter un commentaire */
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @Valid @RequestBody CommentRequest request) {
        try {
            log.info("Adding comment to post {}", id);
            return ResponseEntity.ok(postService.addComment(id, request));
        } catch (RuntimeException e) {
            log.error("Error adding comment to post {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}

