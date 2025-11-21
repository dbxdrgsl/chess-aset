package security;

import network.Request;
import network.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecurityFilterChainTest {

    @Test
    void shouldReturnOkWhenCurrentFilterAuthenticates() {
        AuthenticationProvider provider = request -> true; // mock simplu
        SecurityFilterChain chain = new SecurityFilterChain(provider);

        Request request = new Request("mihai", "pass");

        Response response = chain.doFilter(request);

        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getMessage());
    }

    @Test
    void shouldCallNextFilterWhenCurrentDoesNotAuthenticate() {
        AuthenticationProvider provider = request -> false;
        AuthenticationProvider nextProvider = request -> true;

        SecurityFilterChain chain = new SecurityFilterChain(provider);
        SecurityFilterChain nextChain = new SecurityFilterChain(nextProvider);

        chain.setNextFilter(nextChain);

        Request req = new Request("mihai", "pass");

        Response response = chain.doFilter(req);

        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getMessage());
    }

    @Test
    void shouldReturnUnauthorizedWhenNoFilterAuthenticates() {
        AuthenticationProvider provider = request -> false;

        SecurityFilterChain chain = new SecurityFilterChain(provider);

        Request req = new Request("mihai", "pass");

        Response response = chain.doFilter(req);

        assertEquals(401, response.getStatusCode());
        assertEquals("UNAUTHORIZED", response.getMessage());
    }

    @Test
    void shouldSetNextFilterCorrectly() {
        AuthenticationProvider provider = req -> false;
        AuthenticationProvider nextProvider = req -> true;

        SecurityFilterChain chain = new SecurityFilterChain(provider);
        SecurityFilterChain next = new SecurityFilterChain(nextProvider);

        chain.setNextFilter(next);

        assertNotNull(chain.getNextFilter());
    }
}
