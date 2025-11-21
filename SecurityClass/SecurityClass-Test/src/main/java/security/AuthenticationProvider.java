package security;

import network.Request;

public interface AuthenticationProvider {
    boolean authenticate(Request request);
}
