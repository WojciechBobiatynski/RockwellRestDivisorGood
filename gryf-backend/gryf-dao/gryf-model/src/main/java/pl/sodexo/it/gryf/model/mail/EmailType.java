package pl.sodexo.it.gryf.model.mail;

/**
 * Created by Isolution on 2017-01-16.
 */
public enum EmailType {

    text("text/plain"), html("text/html");

    public static final EmailType DEFAULT_TYPE = text;

    private String contentType;

    EmailType(String contentType){
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
