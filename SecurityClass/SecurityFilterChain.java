package security;

import network.Request;
import network.Response;

public class SecurityFilterChain {

    // --- Attributes ---
    private AuthenticationProvider provider;
    private SecurityFilterChain nextFilter;

    // --- Constructor ---
    public SecurityFilterChain(AuthenticationProvider provider) {
        this.provider = provider;
    }

    // --- Methods ---
    public void setNextFilter(SecurityFilterChain nextFilter);
    public Response doFilter(Request request);
}
