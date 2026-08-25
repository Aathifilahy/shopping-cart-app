package com.example.shoppingcart.security.oauth2;

import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Extract authId, email, name based on provider
        String authId;
        String email;
        String name;

        if ("google".equals(provider)) {
            authId = (String) attributes.get("sub");
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        } else {
            // Facebook and others
            authId = (String) attributes.get("id");
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        }

        System.out.println("🔍 OAuth2 login attempt: provider=" + provider + ", email=" + email + ", authId=" + authId);

        // 1. Try to find user by authProvider + authId (existing user with same provider)
        User user = userRepository.findByAuthProviderAndAuthId(provider, authId)
                .orElseGet(() -> {
                    // 2. If not found, try to find by email (for users who already signed up with another provider)
                    return userRepository.findByEmail(email)
                            .map(existingUser -> {
                                System.out.println("📧 User already exists with email: " + email + " (provider: " + existingUser.getAuthProvider() + ")");
                                // Update the authProvider and authId to allow login with new provider
                                existingUser.setAuthProvider(provider);
                                existingUser.setAuthId(authId);
                                System.out.println("🔄 Updated user with provider: " + provider);
                                return userRepository.save(existingUser);
                            })
                            .orElseGet(() -> {
                                // 3. New user – create it
                                System.out.println("🆕 New user, creating...");
                                User newUser = new User();
                                newUser.setEmail(email);
                                newUser.setName(name);
                                newUser.setAuthProvider(provider);
                                newUser.setAuthId(authId);
                                newUser.setRoles(Set.of("USER"));
                                System.out.println("👤 Saved user with roles: " + newUser.getRoles());
                                return userRepository.save(newUser);
                            });
                });

        System.out.println("✅ Existing/fetched user: " + user.getEmail() + " with roles: " + user.getRoles());

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}