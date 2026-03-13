package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.MessageResponse;
import com.openclassrooms.mddapi.dto.ThemeResponse;
import com.openclassrooms.mddapi.service.ThemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class ThemeController {

    private final ThemeService themeService;

    /**
     * GET /api/themes
     * Retourne tous les thèmes avec le flag "subscribed" selon l'utilisateur connecté.
     */
    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        log.info("Fetching all themes");
        return ResponseEntity.ok(themeService.getAllThemes());
    }

    /**
     * GET /api/themes/subscribed
     * Retourne uniquement les thèmes auxquels l'utilisateur est abonné.
     */
    @GetMapping("/subscribed")
    public ResponseEntity<List<ThemeResponse>> getSubscribedThemes() {
        log.info("Fetching subscribed themes for current user");
        return ResponseEntity.ok(themeService.getSubscribedThemes());
    }

    /**
     * POST /api/themes/{id}/subscribe
     * Abonne l'utilisateur connecté au thème.
     */
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable Long id) {
        try {
            log.info("Subscribing to theme id: {}", id);
            themeService.subscribe(id);
            return ResponseEntity.ok(new MessageResponse("Abonnement au thème effectué avec succès"));
        } catch (RuntimeException e) {
            log.error("Error subscribing to theme {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * DELETE /api/themes/{id}/subscribe
     * Désabonne l'utilisateur connecté du thème.
     */
    @DeleteMapping("/{id}/subscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable Long id) {
        try {
            log.info("Unsubscribing from theme id: {}", id);
            themeService.unsubscribe(id);
            return ResponseEntity.ok(new MessageResponse("Désabonnement du thème effectué avec succès"));
        } catch (RuntimeException e) {
            log.error("Error unsubscribing from theme {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}

