package pl.sodexo.it.gryf.common.dto.security.individuals;

/**
 * Interfejs z danymi do wysyłki maila z danymi do weryfikacji
 *
 * Created by akmiecinski on 04.11.2016.
 */
public interface Verificationable {

    String getLogin();
    String getVerificationCode();
    String getVerificationEmail();

}
