package pl.sodexo.it.gryf.service.api.utils;

/**
 * Created by adziobek on 21.11.2016.
 */
public interface GryfAccessCodeGenerator {

    /**
     * Tworzy nowy kod weryfikacyjny
     * @return
     */
    String createVerificationCode();

    /**
     * Tworzy nowy PIN do szkolenia
     * @return
     */
    String createReimbursmentPin();
}