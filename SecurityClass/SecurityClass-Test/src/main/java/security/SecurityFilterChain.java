package security;

import network.Request;
import network.Response;

import java.util.ArrayList;
import java.util.List;

public class SecurityFilterChain {

    private List<AuthenticationProvider> providers = new ArrayList<>();
    private SecurityFilterChain nextFilter;

    // Constructor cu un singur provider
    public SecurityFilterChain(AuthenticationProvider provider) {
        this.providers.add(provider);
    }

    // Constructor gol - necesar pentru testele tale
    public SecurityFilterChain() {
    }

    // Permite adăugarea de provideri dinamici (cerut de test)
    public void addProvider(AuthenticationProvider provider) {
        this.providers.add(provider);
    }

    public void setNextFilter(SecurityFilterChain nextFilter) {
        this.nextFilter = nextFilter;
    }

    public SecurityFilterChain getNextFilter() {
        return nextFilter;
    }

    // Execută provideri în ordine
    public Response doFilter(Request request) {

        for (AuthenticationProvider provider : providers) {
            boolean authenticated = provider.authenticate(request);
            if (authenticated) {
                return new Response(200, "OK");
            }
        }

        // Dacă providerii din acest lanț nu autentifică, mergem pe nextFilter
        if (nextFilter != null) {
            return nextFilter.doFilter(request);
        }

        // Dacă nici un filtru nu autentifică
        return new Response(401, "UNAUTHORIZED");
    }
}
