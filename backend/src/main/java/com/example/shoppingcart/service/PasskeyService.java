package com.example.shoppingcart.service;

import com.example.shoppingcart.model.PasskeyCredential;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.PasskeyCredentialRepository;
import com.example.shoppingcart.repository.UserRepository;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartAssertionOptions;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.RegistrationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
/*
@Service
@RequiredArgsConstructor
public class PasskeyService {

    private final RelyingParty relyingParty;
    private final PasskeyCredentialRepository credentialRepository;
    private final UserRepository userRepository;

    public PublicKeyCredentialCreationOptions startRegistration(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ByteArray userHandle = new ByteArray(userId.getBytes());

        StartRegistrationOptions options = StartRegistrationOptions.builder()
                .user(new UserIdentity(
                        user.getEmail(),   // name
                        user.getName(),    // displayName
                        userHandle         // id
                ))
                .authenticatorSelection(AuthenticatorSelectionCriteria.builder()
                        .residentKey(ResidentKeyRequirement.REQUIRED)
                        .userVerification(UserVerificationRequirement.PREFERRED)
                        .build())
                .build();

        return relyingParty.startRegistration(options);
    }

    public void completeRegistration(String userId,
                                     PublicKeyCredential<AuthenticatorAttestationResponse> credential)
            throws RegistrationFailedException {
        User user = userRepository.findById(userId).orElseThrow();
        StartRegistrationOptions options = StartRegistrationOptions.builder()
                .user(new UserIdentity(
                        user.getEmail(),
                        user.getName(),
                        new ByteArray(userId.getBytes())
                ))
                .build();

        RegistrationResult result = relyingParty.finishRegistration(options, credential);

        PasskeyCredential pc = new PasskeyCredential();
        pc.setUserId(userId);
        pc.setCredentialId(result.getKeyId().getId().getBase64Url());
        pc.setPublicKeyCose(result.getPublicKeyCose().getBase64Url());
        pc.setSignCount(result.getSignatureCount());
        credentialRepository.save(pc);
    }

    public AssertionRequest startLogin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var credentials = credentialRepository.findAll().stream()
                .filter(c -> c.getUserId().equals(user.getId()))
                .map(c -> PublicKeyCredentialDescriptor.builder()
                        .id(new ByteArray(c.getCredentialId().getBytes()))
                        .type(PublicKeyCredentialType.PUBLIC_KEY)
                        .build())
                .collect(Collectors.toList());

        return relyingParty.startAssertion(StartAssertionOptions.builder()
                .username(user.getEmail())
                .allowedCredentials(credentials)   // corrected method name
                .build());
    }
}
    */