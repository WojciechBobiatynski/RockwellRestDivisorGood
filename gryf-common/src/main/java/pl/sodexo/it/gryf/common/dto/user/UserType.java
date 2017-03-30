package pl.sodexo.it.gryf.common.dto.user;

/**
 * Tryb pracy zalogowanego uzytkownika.
 *
 * Created by adziobek on 28.09.2016.
 */
public enum UserType {

    /**
     * Operator finansowy
     */
    FINANCIAL_OPERATOR("FOU"),
    
    /**
     * Usługodawca
     */
    TRAINING_INSTITUTION("TIU"),

    /**
     * Osoba fizyczna
     */
    INDIVIDUAL("INDU");

    private String code;

    UserType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
