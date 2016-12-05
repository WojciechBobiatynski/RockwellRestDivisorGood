package pl.sodexo.it.gryf.common.exception;

import lombok.ToString;

/**
 * Created by tomasz.bilski.ext on 2015-06-26.
 */
@ToString
public class EntityConstraintViolation {

    //FIELDS

    private String path;

    private String message;

    private Object invalidValue;

    //CONSTRUCTIONS

    public EntityConstraintViolation(String message) {
        this(null, message, null);
    }

    public EntityConstraintViolation(String path, String message) {
        this(path, message, null);
    }

    public EntityConstraintViolation(String path, String message, Object invalidValue) {
        this.path = path;
        this.message = message;
        this.invalidValue = invalidValue;
    }

    //GETTERS

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
    }
}
