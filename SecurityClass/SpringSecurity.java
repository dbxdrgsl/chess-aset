package security;

import network.Request;
import network.Response;

public class SpringSecurity {

    // --- Attributes ---
    private AuthenticationProvider authProvider;  // Strategy Pattern
    private SecurityFilterChain filterChain;      // Chain of Responsibility Pattern
    private Logger logger;                        // pentru AOP (interceptarea evenimentelor de securitate)

    // --- Constructor ---
    public SpringSecurity(AuthenticationProvider provider, SecurityFilterChain chain) {
        this.authProvider = provider;
        this.filterChain = chain;
    }

    // --- Methods ---
    public boolean authenticateRequest(Request request);
    public boolean authorizeAction(String userRole, String action);
    public Response processSecureRequest(Request request);
    public void logSecurityEvent(String event);
}
