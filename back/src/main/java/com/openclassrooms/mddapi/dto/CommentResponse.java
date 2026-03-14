package com.openclassrooms.mddapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;
}

