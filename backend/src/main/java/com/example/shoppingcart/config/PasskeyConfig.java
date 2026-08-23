package com.example.shoppingcart.config;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
/* 
@Configuration
public class PasskeyConfig {

    @Value("${app.passkey.rp-id:localhost}")
    private String rpId;

    @Value("${app.passkey.rp-name:ShoppingCartApp}")
    private String rpName;

    //@Bean
    public RelyingParty relyingParty() {
        return RelyingParty.builder()
                .identity(RelyingPartyIdentity.builder()
                        .id(rpId)
                        .name(rpName)
                        .build())
                .preferredOrigins(Collections.singletonList("http://localhost:8080"))
                .build();
    }
}

*/