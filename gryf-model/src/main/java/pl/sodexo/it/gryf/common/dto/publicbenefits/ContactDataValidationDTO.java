package pl.sodexo.it.gryf.common.dto.publicbenefits;

/**
 * Created by tomasz.bilski.ext on 2015-06-29.
 */
public class ContactDataValidationDTO {

    //FIELDS

    private boolean valid;

    private String message;

    //CONSTRUCTORS


    public ContactDataValidationDTO() {
        this(true, null);
    }

    public ContactDataValidationDTO(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }


    //GETTERS & SETTERS

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
