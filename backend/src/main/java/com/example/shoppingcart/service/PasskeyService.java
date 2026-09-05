package com.example.shoppingcart.service;

import com.example.shoppingcart.model.PasskeyCredential;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.PasskeyCredentialRepository;
import com.example.shoppingcart.repository.UserRepository;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartAssertionOptions;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.AuthenticatorSelectionCriteria;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.ResidentKeyRequirement;
import com.yubico.webauthn.data.UserIdentity;
import com.yubico.webauthn.data.UserVerificationRequirement;
import com.yubico.webauthn.exception.RegistrationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PasskeyService {

    private final RelyingParty relyingParty;
    private final PasskeyCredentialRepository credentialRepository;
    private final UserRepository userRepository;

    // In-memory store for registration requests (keyed by email)
    private final ConcurrentHashMap<String, PublicKeyCredentialCreationOptions> registrationRequests = new ConcurrentHashMap<>();

    public PublicKeyCredentialCreationOptions startRegistration(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        ByteArray userHandle = new ByteArray(user.getId().getBytes(StandardCharsets.UTF_8));

        UserIdentity userIdentity = UserIdentity.builder()
                .name(user.getEmail())
                .displayName(user.getName())
                .id(userHandle)
                .build();

        StartRegistrationOptions options = StartRegistrationOptions.builder()
                .user(userIdentity)
                .authenticatorSelection(
                        AuthenticatorSelectionCriteria.builder()
                                .residentKey(ResidentKeyRequirement.REQUIRED)
                                .userVerification(UserVerificationRequirement.PREFERRED)
                                .build()
                )
                .build();

        PublicKeyCredentialCreationOptions creationOptions = relyingParty.startRegistration(options);
        registrationRequests.put(email, creationOptions);
        return creationOptions;
    }

    public void completeRegistration(
            String email,
            PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> credential
    ) throws RegistrationFailedException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        PublicKeyCredentialCreationOptions request = registrationRequests.remove(email);
        if (request == null) {
            throw new RuntimeException("No active registration request for email: " + email);
        }

        FinishRegistrationOptions finishOptions = FinishRegistrationOptions.builder()
                .request(request)
                .response(credential)
                .build();

        RegistrationResult result = relyingParty.finishRegistration(finishOptions);

        PasskeyCredential passkeyCredential = new PasskeyCredential();
        passkeyCredential.setUserId(user.getId());
        passkeyCredential.setCredentialId(result.getKeyId().getId().getBase64Url());
        passkeyCredential.setPublicKeyCose(result.getPublicKeyCose().getBase64Url());
        passkeyCredential.setSignCount(result.getSignatureCount());

        credentialRepository.save(passkeyCredential);
    }

    public AssertionRequest startLogin(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        StartAssertionOptions options = StartAssertionOptions.builder()
                .username(email)
                .build();

        return relyingParty.startAssertion(options);
    }
}