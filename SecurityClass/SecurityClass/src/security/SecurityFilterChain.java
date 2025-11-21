package security;

import network.Request;
import network.Response;

public class SecurityFilterChain {

    private AuthenticationProvider provider;
    private SecurityFilterChain nextFilter;

    public SecurityFilterChain(AuthenticationProvider provider) {
        this.provider = provider;
    }

    public void setNextFilter(SecurityFilterChain nextFilter) {
        this.nextFilter = nextFilter;
    }

    public Response doFilter(Request request) {
        return null;
    }
}
