package com.openclassrooms.mddapi.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private String themeTitle;
    private Long themeId;
    private LocalDateTime createdAt;
    private List<CommentResponse> comments;
}

