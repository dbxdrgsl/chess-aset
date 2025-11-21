package security;

import network.Request;
import network.Response;

public class SpringSecurity {

    private SecurityFilterChain filterChain;

    public SpringSecurity(SecurityFilterChain filterChain) {
        this.filterChain = filterChain;
    }

    public boolean authenticate(Request request) {
        Response response = filterChain.doFilter(request);
        return response.getStatusCode() == 200;
    }

    public void setFilterChain(SecurityFilterChain chain) {
        this.filterChain = chain;
    }
}
