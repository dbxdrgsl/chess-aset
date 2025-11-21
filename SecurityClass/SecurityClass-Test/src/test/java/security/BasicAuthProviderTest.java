package security;

import network.Request;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BasicAuthProviderTest {

    @Test
    void shouldAuthenticateWhenCredentialsAreCorrect() {
        BasicAuthProvider auth = new BasicAuthProvider("mihai", "pass");

        Request request = new Request("mihai", "pass");

        assertTrue(auth.authenticate(request));
    }

    @Test
    void shouldNotAuthenticateWhenCredentialsAreIncorrect() {
        BasicAuthProvider auth = new BasicAuthProvider("mihai", "pass");

        Request request = new Request("mihai", "WRONG");

        assertFalse(auth.authenticate(request));
    }
}
