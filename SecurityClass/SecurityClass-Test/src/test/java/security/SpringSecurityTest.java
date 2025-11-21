package security;

import network.Request;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpringSecurityTest {

    @Test
    public void shouldAuthenticateUsingFilterChain() {
        // mock Request
        Request request = new Request("user", "pass");

        // mock filter chain
        SecurityFilterChain chain = new SecurityFilterChain();
        chain.addProvider(req -> true); // Accept ALWAYS

        // system under test
        SpringSecurity security = new SpringSecurity(chain);

        assertTrue(security.authenticate(request));
    }

    @Test
    public void shouldFailAuthenticationWhenChainRejects() {
        Request request = new Request("user", "wrong");

        SecurityFilterChain chain = new SecurityFilterChain();
        chain.addProvider(req -> false); // Reject ALWAYS

        SpringSecurity security = new SpringSecurity(chain);

        assertFalse(security.authenticate(request));
    }

    @Test
    public void shouldAllowChangingFilterChain() {
        Request request = new Request("user", "pass");

        SecurityFilterChain chain1 = new SecurityFilterChain();
        chain1.addProvider(req -> false);

        SecurityFilterChain chain2 = new SecurityFilterChain();
        chain2.addProvider(req -> true);

        SpringSecurity security = new SpringSecurity(chain1);
        assertFalse(security.authenticate(request)); // initial chain fails

        security.setFilterChain(chain2);
        assertTrue(security.authenticate(request));  // new chain passes
    }
}
