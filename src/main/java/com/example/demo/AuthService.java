package com.example.demo;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String register(String username, String password) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new InvalidPasswordException();
        }
        String normalizedUsername = normalizeUsername(username);
        if (appUserRepository.existsByUsername(normalizedUsername)) {
            throw new UsernameAlreadyExistsException();
        }

        AppUser user = new AppUser(normalizedUsername, passwordEncoder.encode(password));
        try {
            appUserRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UsernameAlreadyExistsException();
        }
        return normalizedUsername;
    }

    @Transactional(readOnly = true)
    public boolean authenticate(String username, String password) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            return false;
        }
        return appUserRepository.findByUsername(normalizeUsername(username))
                .map(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .orElse(false);
    }

    private String normalizeUsername(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }
}
