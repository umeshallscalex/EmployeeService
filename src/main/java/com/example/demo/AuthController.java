package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody Credentials credentials) {
        String username = authService.register(credentials.username(), credentials.password());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("Account created.", username));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody Credentials credentials,
            HttpServletRequest request) {
        if (!authService.authenticate(credentials.username(), credentials.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid username or password.", null));
        }
        HttpSession session = request.getSession(true);
        request.changeSessionId();
        session.setAttribute("authenticatedUsername",
                credentials.username().trim().toLowerCase(Locale.ROOT));
        return ResponseEntity.ok(new AuthResponse("Login successful.", credentials.username()));
    }

    @GetMapping("/session")
    public SessionResponse session(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return new SessionResponse(false, null);
        }
        Object username = session.getAttribute("authenticatedUsername");
        return new SessionResponse(username instanceof String,
                username instanceof String ? (String) username : null);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new AuthResponse("Logged out.", null));
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<AuthResponse> usernameAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AuthResponse("Username is already registered.", null));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<AuthResponse> passwordTooLong() {
        return ResponseEntity.badRequest()
                .body(new AuthResponse("Password must be no longer than 72 UTF-8 bytes.", null));
    }

    public record Credentials(
            @NotBlank
            @Size(min = 3, max = 50)
            @Pattern(regexp = "^[A-Za-z0-9._-]+$")
            String username,
            @NotBlank
            @Size(min = 8, max = 72)
            String password) {
    }

    public record AuthResponse(String message, String username) {
    }

    public record SessionResponse(boolean authenticated, String username) {
    }
}
