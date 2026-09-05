package com.example.shoppingcart.config;

import com.example.shoppingcart.security.passkey.PasskeyCredentialRepositoryAdapter;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class PasskeyConfig {

    @Value("${app.passkey.rp-id:localhost}")
    private String rpId;

    @Value("${app.passkey.rp-name:ShoppingCartApp}")
    private String rpName;

    @Bean
    public RelyingParty relyingParty(
            PasskeyCredentialRepositoryAdapter credentialRepository
    ) {
        return RelyingParty.builder()
                .identity(
                        RelyingPartyIdentity.builder()
                                .id(rpId)
                                .name(rpName)
                                .build()
                )
                .credentialRepository(credentialRepository)
                .origins(Set.of("http://localhost:8080"))
                .build();
    }
}