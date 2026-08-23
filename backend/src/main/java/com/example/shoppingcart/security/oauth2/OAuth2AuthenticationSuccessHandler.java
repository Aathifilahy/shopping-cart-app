package com.example.shoppingcart.security.oauth2;

import com.example.shoppingcart.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("OAuth2AuthenticationSuccessHandler invoked!"); // <-- ADD THIS

        String token = tokenProvider.generateToken(authentication);
        String redirectUrl = "http://localhost:3000/oauth2/redirect?token=" + token;

        System.out.println("🔗 Redirecting to: " + redirectUrl); // <-- ADD THIS

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}