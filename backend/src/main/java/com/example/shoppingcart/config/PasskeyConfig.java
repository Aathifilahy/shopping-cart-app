package com.example.shoppingcart.config;

import com.example.shoppingcart.security.passkey.PasskeyCredentialRepositoryAdapter;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class PasskeyConfig {

    @Value("${app.passkey.rp-id:localhost}")
    private String rpId;

    @Value("${app.passkey.rp-name:ShoppingCartApp}")
    private String rpName;

    // Read comma‑separated origins from environment or use defaults
    @Value("${app.passkey.origins:http://localhost:3000,http://localhost:8080}")
    private String originsString;

    @Bean
    public RelyingParty relyingParty(
            PasskeyCredentialRepositoryAdapter credentialRepository
    ) {
        // Convert the comma‑separated string into a Set of URIs
        Set<URI> origins = Arrays.stream(originsString.split(","))
                .map(String::trim)
                .map(URI::create)
                .collect(Collectors.toSet());

        return RelyingParty.builder()
                .identity(
                        RelyingPartyIdentity.builder()
                                .id(rpId)
                                .name(rpName)
                                .build()
                )
                .credentialRepository(credentialRepository)
                .origins(origins)
                .build();
    }
}