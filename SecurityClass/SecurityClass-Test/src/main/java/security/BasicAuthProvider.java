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
//        return false;                                                                                             // RED stage în TDD
        return request.getUsername().equals(validUsername) && request.getPassword().equals(validPassword);        // GREEN stage în TDD
    }
}
