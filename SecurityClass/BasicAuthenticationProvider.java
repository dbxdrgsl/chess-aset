package security;

import network.Request;

public class BasicAuthProvider implements AuthenticationProvider {

    // --- Attributes ---
    private String validUsername;
    private String validPassword;

    // --- Constructor ---
    public BasicAuthProvider(String username, String password) {
        this.validUsername = username;
        this.validPassword = password;
    }

    // --- Method ---
    @Override
    public boolean authenticate(Request request);
}
