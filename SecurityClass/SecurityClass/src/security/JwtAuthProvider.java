package security;

import network.Request;

public class JwtAuthProvider implements AuthenticationProvider {

    private String secretKey;

    public JwtAuthProvider(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean authenticate(Request request) {
        return false;
    }
}
