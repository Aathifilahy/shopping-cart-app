package com.example.shoppingcart.security.passkey;

import com.example.shoppingcart.model.PasskeyCredential;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.PasskeyCredentialRepository;
import com.example.shoppingcart.repository.UserRepository;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.PublicKeyCredentialType;
import com.yubico.webauthn.data.exception.Base64UrlException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PasskeyCredentialRepositoryAdapter
        implements CredentialRepository {

    private final PasskeyCredentialRepository credentialRepository;
    private final UserRepository userRepository;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(
            String username
    ) {
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            return Collections.emptySet();
        }

        Set<PublicKeyCredentialDescriptor> descriptors = new HashSet<>();

        credentialRepository.findAll()
                .stream()
                .filter(credential ->
                        credential.getUserId().equals(user.get().getId()))
                .forEach(credential -> {
                    ByteArray credentialId =
                            decodeBase64Url(credential.getCredentialId());

                    descriptors.add(
                            PublicKeyCredentialDescriptor.builder()
                                    .id(credentialId)
                                    .type(PublicKeyCredentialType.PUBLIC_KEY)
                                    .build()
                    );
                });

        return descriptors;
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return userRepository.findByEmail(username)
                .map(user -> new ByteArray(
                        user.getId().getBytes(StandardCharsets.UTF_8)
                ));
    }

    @Override
    public Optional<String> getUsernameForUserHandle(
            ByteArray userHandle
    ) {
        String userId = new String(
                userHandle.getBytes(),
                StandardCharsets.UTF_8
        );

        return userRepository.findById(userId)
                .map(User::getEmail);
    }

    @Override
    public Optional<RegisteredCredential> lookup(
            ByteArray credentialId,
            ByteArray userHandle
    ) {
        Optional<PasskeyCredential> credential =
                credentialRepository.findByCredentialId(
                        credentialId.getBase64Url()
                );

        if (credential.isEmpty()) {
            return Optional.empty();
        }

        PasskeyCredential storedCredential = credential.get();

        ByteArray storedUserHandle = new ByteArray(
                storedCredential.getUserId()
                        .getBytes(StandardCharsets.UTF_8)
        );

        if (!storedUserHandle.equals(userHandle)) {
            return Optional.empty();
        }

        return Optional.of(toRegisteredCredential(storedCredential));
    }

    @Override
    public Set<RegisteredCredential> lookupAll(
            ByteArray credentialId
    ) {
        Optional<PasskeyCredential> credential =
                credentialRepository.findByCredentialId(
                        credentialId.getBase64Url()
                );

        if (credential.isEmpty()) {
            return Collections.emptySet();
        }

        return Set.of(toRegisteredCredential(credential.get()));
    }

    private RegisteredCredential toRegisteredCredential(
            PasskeyCredential credential
    ) {
        return RegisteredCredential.builder()
                .credentialId(
                        decodeBase64Url(
                                credential.getCredentialId()
                        )
                )
                .userHandle(
                        new ByteArray(
                                credential.getUserId()
                                        .getBytes(StandardCharsets.UTF_8)
                        )
                )
                .publicKeyCose(
                        decodeBase64Url(
                                credential.getPublicKeyCose()
                        )
                )
                .signatureCount(credential.getSignCount())
                .build();
    }

    private ByteArray decodeBase64Url(String value) {
        try {
            return ByteArray.fromBase64Url(value);
        } catch (Base64UrlException exception) {
            throw new IllegalArgumentException(
                    "Invalid Base64URL passkey value",
                    exception
            );
        }
    }
}