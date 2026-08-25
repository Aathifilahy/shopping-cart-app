package com.example.shoppingcart.service;

import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

        // Debug logs – will appear in Render logs
        System.out.println("🔍 Loading user: " + email);
        System.out.println("👤 Roles from DB: " + user.getRoles());
        System.out.println("🔑 Authorities: " + authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                "",
                authorities
        );
    }
}