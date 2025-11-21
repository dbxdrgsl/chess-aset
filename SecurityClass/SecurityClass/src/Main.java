import network.Request;
import security.BasicAuthProvider;
import security.AuthenticationProvider;

public class Main {
    public static void main(String[] args) {

        AuthenticationProvider provider = new BasicAuthProvider("admin", "1234");

        Request request = new Request();

        boolean ok = provider.authenticate(request);

        System.out.println("Auth result = " + ok);
    }
}
