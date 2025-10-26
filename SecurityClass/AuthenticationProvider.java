// interfata de baza pentru - Strategy Pattern

package security;

import network.Request;

public interface AuthenticationProvider {
    boolean authenticate(Request request);
}
