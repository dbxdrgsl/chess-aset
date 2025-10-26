package security;

import network.Request;

public class JwtAuthProvider implements AuthenticationProvider {

    // --- Attributes ---
    private String secretKey;

    // --- Constructor ---
    public JwtAuthProvider(String secretKey) {
        this.secretKey = secretKey;
    }

    // --- Method ---
    @Override
    public boolean authenticate(Request request);
}
