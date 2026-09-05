package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.UserRepository;
import com.example.shoppingcart.security.jwt.JwtTokenProvider;
import com.example.shoppingcart.security.oauth2.CustomOAuth2User;
import com.example.shoppingcart.service.PasskeyService;  // 👈 add this import
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;  // 👈 add this import for request bodies

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasskeyService passkeyService;  // 👈 add this field

    // OAuth2 endpoints handled by Spring Security's success handler

    // ---------- Passkey endpoints ----------
    @PostMapping("/passkey/register/start")
    public ResponseEntity<?> startPasskeyRegistration(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        return ResponseEntity.ok(passkeyService.startRegistration(userId));
    }

    @PostMapping("/passkey/register/complete")
    public ResponseEntity<?> completePasskeyRegistration(@RequestBody Map<String, Object> body) {
        // In a real implementation, deserialize the credential from the body.
        // For now, we return a placeholder.
        // To fully implement, you would extract the credential and call:
        // passkeyService.completeRegistration(userId, credential);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/passkey/login/start")
    public ResponseEntity<?> startPasskeyLogin(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return ResponseEntity.ok(passkeyService.startLogin(email));
    }

    @PostMapping("/passkey/login/complete")
    public ResponseEntity<?> completePasskeyLogin(@RequestBody Map<String, Object> body) {
        // Deserialize the credential and perform authentication.
        // This would involve creating a PasskeyAuthenticationToken and authenticating it.
        // For now, we return a placeholder.
        return ResponseEntity.ok().build();
    }

    // ---------- Current user endpoint ----------
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