package com.example.shoppingcart.config;

import com.example.shoppingcart.security.passkey.PasskeyCredentialRepositoryAdapter;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class PasskeyConfig {

    @Value("${app.passkey.rp-id:localhost}")
    private String rpId;

    @Value("${app.passkey.rp-name:ShoppingCartApp}")
    private String rpName;

    @Value("${app.passkey.origins:http://localhost:3000,http://localhost:8080}")
    private String originsString;

    @Bean
    public RelyingParty relyingParty(
            PasskeyCredentialRepositoryAdapter credentialRepository
    ) {
        // Convert comma‑separated string into a Set<String>
        Set<String> origins = Arrays.stream(originsString.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        return RelyingParty.builder()
                .identity(
                        RelyingPartyIdentity.builder()
                                .id(rpId)
                                .name(rpName)
                                .build()
                )
                .credentialRepository(credentialRepository)
                .origins(origins)   // now passing Set<String>
                .build();
    }
}