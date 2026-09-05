package com.example.shoppingcart.security.passkey;

import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PasskeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    private final PublicKeyCredential<
            AuthenticatorAssertionResponse,
            ClientAssertionExtensionOutputs
            > credential;
    private final AssertionRequest request;

    public PasskeyAuthenticationToken(
            String email,
            PublicKeyCredential<
                    AuthenticatorAssertionResponse,
                    ClientAssertionExtensionOutputs
                    > credential,
            AssertionRequest request,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.email = email;
        this.credential = credential;
        this.request = request;
        setAuthenticated(true);
    }

    public PasskeyAuthenticationToken(
            String email,
            PublicKeyCredential<
                    AuthenticatorAssertionResponse,
                    ClientAssertionExtensionOutputs
                    > credential,
            AssertionRequest request
    ) {
        super(null);
        this.email = email;
        this.credential = credential;
        this.request = request;
        setAuthenticated(false);
    }

    public PublicKeyCredential<
            AuthenticatorAssertionResponse,
            ClientAssertionExtensionOutputs
            > getCredential() {
        return credential;
    }

    public AssertionRequest getRequest() {
        return request;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }
}