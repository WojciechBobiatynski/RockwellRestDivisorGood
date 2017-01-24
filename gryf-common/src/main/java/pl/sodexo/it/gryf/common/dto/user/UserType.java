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
    FINANCIAL_OPERATOR,
    
    /**
     * Usługodawca
     */
    TRAINING_INSTITUTION,

    /**
     * Osoba fizyczna
     */
    INDIVIDUAL,
}
