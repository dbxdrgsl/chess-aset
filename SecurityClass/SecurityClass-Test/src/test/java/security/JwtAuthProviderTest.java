package security;

import network.Request;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthProviderTest {

    @Test
    void shouldGenerateValidTokenAndAuthenticate() {
        JwtAuthProvider provider = new JwtAuthProvider("secret-key");

        // 1. Generate token
        String token = provider.generateToken("mihai");

        // 2. Request containing the token in password field
        Request request = new Request("mihai", token);

        // 3. Must authenticate
        assertTrue(provider.authenticate(request));
    }

    @Test
    void shouldNotAuthenticateWithInvalidToken() {
        JwtAuthProvider provider = new JwtAuthProvider("secret-key");

        // Wrong token
        Request request = new Request("mihai", "INVALID.TOKEN");

        assertFalse(provider.authenticate(request));
    }

    @Test
    void shouldNotAuthenticateIfTokenFormatIsInvalid() {
        JwtAuthProvider provider = new JwtAuthProvider("secret-key");

        // No dot
        Request request = new Request("mihai", "invalidtokenwithoutdot");

        assertFalse(provider.authenticate(request));
    }
}
