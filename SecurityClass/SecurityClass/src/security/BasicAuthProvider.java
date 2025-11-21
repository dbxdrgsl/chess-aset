package security;

import network.Request;

public class BasicAuthProvider implements AuthenticationProvider {

    private String validUsername;
    private String validPassword;

    public BasicAuthProvider(String username, String password) {
        this.validUsername = username;
        this.validPassword = password;
    }

    @Override
    public boolean authenticate(Request request) {
        // implementarea o lăsăm goală pentru că LAB 5 cere DOAR DECLARAȚII
        return false;
    }
}
