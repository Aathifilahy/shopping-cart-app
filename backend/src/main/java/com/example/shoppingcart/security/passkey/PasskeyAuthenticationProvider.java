package com.example.shoppingcart.security.passkey;

import com.example.shoppingcart.model.PasskeyCredential;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.PasskeyCredentialRepository;
import com.example.shoppingcart.repository.UserRepository;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.FinishAssertionOptions;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PasskeyAuthenticationProvider implements AuthenticationProvider {

    private final RelyingParty relyingParty;
    private final PasskeyCredentialRepository credentialRepository;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        if (!(authentication instanceof PasskeyAuthenticationToken token)) {
            return null;
        }

        try {
            PublicKeyCredential<
                    AuthenticatorAssertionResponse,
                    ClientAssertionExtensionOutputs
                    > credential = token.getCredential();

            FinishAssertionOptions options = FinishAssertionOptions.builder()
                    .request(token.getRequest())
                    .response(credential)
                    .build();

            AssertionResult result = relyingParty.finishAssertion(options);

            if (!result.isSuccess()) {
                throw new BadCredentialsException("Passkey authentication failed");
            }

            String credentialId = result.getCredentialId().getBase64Url();

            PasskeyCredential storedCredential =
                    credentialRepository.findByCredentialId(credentialId)
                            .orElseThrow(() ->
                                    new BadCredentialsException(
                                            "Passkey credential not found"
                                    ));

            User user = userRepository.findById(storedCredential.getUserId())
                    .orElseThrow(() ->
                            new BadCredentialsException("User not found"));

            storedCredential.setSignCount(result.getSignatureCount());
            credentialRepository.save(storedCredential);

            String role = user.getRoles()
                    .stream()
                    .findFirst()
                    .orElse("USER");

            return new PasskeyAuthenticationToken(
                    user.getEmail(),
                    null,
                    null,
                    Set.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

        } catch (BadCredentialsException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BadCredentialsException(
                    "Passkey authentication failed",
                    exception
            );
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasskeyAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}