package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ThemeResponse;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ThemeResponse> getAllThemes() {
        User currentUser = getCurrentUserEntity();
        Set<Long> subscribedIds = currentUser.getSubscribedThemes()
                .stream()
                .map(Theme::getId)
                .collect(Collectors.toSet());

        return themeRepository.findAll().stream()
                .map(theme -> ThemeResponse.builder()
                        .id(theme.getId())
                        .title(theme.getTitle())
                        .description(theme.getDescription())
                        .subscribed(subscribedIds.contains(theme.getId()))
                        .createdAt(theme.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> getSubscribedThemes() {
        User currentUser = getCurrentUserEntity();
        return currentUser.getSubscribedThemes().stream()
                .map(theme -> ThemeResponse.builder()
                        .id(theme.getId())
                        .title(theme.getTitle())
                        .description(theme.getDescription())
                        .subscribed(true)
                        .createdAt(theme.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void subscribe(Long themeId) {
        User currentUser = getCurrentUserEntity();
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("Thème non trouvé avec l'id : " + themeId));
        currentUser.getSubscribedThemes().add(theme);
        userRepository.save(currentUser);
        log.info("User {} subscribed to theme {}", currentUser.getUsername(), theme.getTitle());
    }

    @Transactional
    public void unsubscribe(Long themeId) {
        User currentUser = getCurrentUserEntity();
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("Thème non trouvé avec l'id : " + themeId));
        currentUser.getSubscribedThemes().remove(theme);
        userRepository.save(currentUser);
        log.info("User {} unsubscribed from theme {}", currentUser.getUsername(), theme.getTitle());
    }

    private User getCurrentUserEntity() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}

