package pl.sodexo.it.gryf.web.fo.response;

public class SimpleMessageResponse {
    private final String message;

    public SimpleMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
