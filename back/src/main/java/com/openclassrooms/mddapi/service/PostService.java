package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.dto.PostRequest;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<PostResponse> getFeed() {
        User currentUser = getCurrentUserEntity();
        Set<Theme> subscribedThemes = currentUser.getSubscribedThemes();
        if (subscribedThemes.isEmpty()) {
            return List.of();
        }
        return postRepository.findByThemeInOrderByCreatedAtDesc(subscribedThemes)
                .stream()
                .map(this::mapToPostSummary)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse createPost(PostRequest request) {
        User currentUser = getCurrentUserEntity();
        Theme theme = themeRepository.findById(request.getThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Thème non trouvé avec l'id : " + request.getThemeId()));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(currentUser)
                .theme(theme)
                .build();

        Post saved = postRepository.save(post);
        log.info("Article créé par {} dans le thème {}", currentUser.getUsername(), theme.getTitle());
        return mapToPostSummary(saved);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'id : " + id));

        List<CommentResponse> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(id)
                .stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());

        PostResponse response = mapToPostSummary(post);
        response.setComments(comments);
        return response;
    }

    @Transactional
    public CommentResponse addComment(Long postId, CommentRequest request) {
        User currentUser = getCurrentUserEntity();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'id : " + postId));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(currentUser)
                .post(post)
                .build();

        Comment saved = commentRepository.save(comment);
        log.info("Commentaire ajouté par {} sur l'article {}", currentUser.getUsername(), postId);
        return mapToCommentResponse(saved);
    }

    private PostResponse mapToPostSummary(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorUsername(post.getAuthor().getUsername())
                .themeTitle(post.getTheme().getTitle())
                .themeId(post.getTheme().getId())
                .createdAt(post.getCreatedAt())
                .comments(new ArrayList<>())
                .build();
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorUsername(comment.getAuthor().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private User getCurrentUserEntity() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}

