package network;

public class Request {

    private String username;
    private String password;

    public Request(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
