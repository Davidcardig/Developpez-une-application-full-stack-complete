package com.openclassrooms.mddapi.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NotBlank(message = "Le contenu du commentaire est requis")
    private String content;
}

