package security;

import network.Request;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JwtAuthProvider implements AuthenticationProvider {

    private final String secret;

    public JwtAuthProvider(String secret) {
        this.secret = secret;
    }

    // --------------------------------------------------
    //  TDD METHOD: AUTHENTICATE A REQUEST
    // --------------------------------------------------
    @Override
    public boolean authenticate(Request request) {
        // În proiectul tău token-ul este trimis în loc de parolă
        String token = request.getPassword();

//        return false;                                                     // RED stage în TDD
        return validateToken(token);                                      // GREEN stage în TDD
    }

    // --------------------------------------------------
    //  Generate a token from username + secret
    // --------------------------------------------------
    public String generateToken(String username) {
        String hash = hash(username + secret);
        return username + "." + hash;
    }

    // --------------------------------------------------
    //  Validate token format and hash
    // --------------------------------------------------
    public boolean validateToken(String token) {
        if (token == null || !token.contains(".")) {
            return false;
        }

        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            return false;
        }

        String username = parts[0];
        String hash = parts[1];

        String expectedHash = hash(username + secret);

        return hash.equals(expectedHash);
    }

    // --------------------------------------------------
    //  Simple SHA-256 + Base64 hash helper
    // --------------------------------------------------
    private String hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

