package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.UserRepository;
import com.example.shoppingcart.security.jwt.JwtTokenProvider;
import com.example.shoppingcart.security.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;   // 👈 added

    // OAuth2 endpoints are handled by Spring Security's success handler

    // Passkey endpoints are commented out (temporarily disabled)
    /*
    @PostMapping("/passkey/register/start")
    public ResponseEntity<?> startPasskeyRegistration(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        return ResponseEntity.ok(passkeyService.startRegistration(userId));
    }
    // ... other passkey endpoints
    */

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Object principal = authentication.getPrincipal();

        // 1. If the user logged in via OAuth2 (Google/Facebook)
        if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User oauthUser = (CustomOAuth2User) principal;
            User user = oauthUser.getUser();
            return ResponseEntity.ok(user); // returns full User entity with roles
        }

        // 2. For other auth methods, extract email and fetch from DB
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        if (email != null) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
        }

        // 3. Fallback: return principal as-is
        return ResponseEntity.ok(principal);
    }
}