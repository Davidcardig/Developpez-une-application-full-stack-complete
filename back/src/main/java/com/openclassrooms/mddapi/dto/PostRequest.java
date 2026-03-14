package com.openclassrooms.mddapi.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {

    @NotNull(message = "Le thème est requis")
    private Long themeId;

    @NotBlank(message = "Le titre est requis")
    @Size(max = 200, message = "Le titre ne peut pas dépasser 200 caractères")
    private String title;

    @NotBlank(message = "Le contenu est requis")
    private String content;
}

