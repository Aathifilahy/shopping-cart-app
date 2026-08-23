package com.example.shoppingcart.security.passkey;

import com.example.shoppingcart.model.PasskeyCredential;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.PasskeyCredentialRepository;
import com.example.shoppingcart.repository.UserRepository;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.PublicKeyCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
/* 
@Component
@RequiredArgsConstructor
public class PasskeyAuthenticationProvider implements AuthenticationProvider {

    private final RelyingParty relyingParty;
    private final PasskeyCredentialRepository credentialRepository;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof PasskeyAuthenticationToken)) {
            return null;
        }

        PasskeyAuthenticationToken token = (PasskeyAuthenticationToken) authentication;
        PublicKeyCredential<AuthenticatorAssertionResponse> credential = token.getCredential();

        try {
            AssertionRequest request = token.getRequest();
            AssertionResult result = relyingParty.finishAssertion(request, credential);

            if (result.isSuccess()) {
                String credentialId = result.getCredentialId().getBase64Url();
                Optional<PasskeyCredential> storedCred = credentialRepository.findByCredentialId(credentialId);
                if (storedCred.isPresent()) {
                    User user = userRepository.findById(storedCred.get().getUserId())
                            .orElseThrow(() -> new BadCredentialsException("User not found"));
                    storedCred.get().setSignCount(result.getSignatureCount());
                    credentialRepository.save(storedCred.get());

                    return new PasskeyAuthenticationToken(
                            user.getEmail(),
                            null,
                            null,
                            Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRoles().iterator().next()))
                    );
                }
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Passkey authentication failed", e);
        }
        throw new BadCredentialsException("Passkey authentication failed");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasskeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

*/