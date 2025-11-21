package network;

public class Response {

    private int statusCode;
    private String message;

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    // getters
    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
