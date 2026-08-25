package com.example.shoppingcart.security.oauth2;

import com.example.shoppingcart.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("OAuth2AuthenticationSuccessHandler invoked!");
        String token = tokenProvider.generateToken(authentication);
        String redirectUrl = frontendUrl + "/oauth2/redirect?token=" + token;
        System.out.println("🔗 Redirecting to: " + redirectUrl);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}