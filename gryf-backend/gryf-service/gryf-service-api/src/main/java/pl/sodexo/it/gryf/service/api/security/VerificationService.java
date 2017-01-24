package pl.sodexo.it.gryf.service.api.security;

import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;
import pl.sodexo.it.gryf.common.exception.verification.GryfVerificationException;

/**
 * Serwis obsługujący zdarzenia związane z weryfikacją osoby fizycznej
 *
 * Created by akmiecinski on 17.10.2016.
 */
public interface VerificationService {

    /**
     * Metoda przesyłająca nowy kod weryfikacyjny
     * @param verificationDto - obiekt z danymi do weryfikacji
     * @throws GryfVerificationException - wyjątek dla błędnych danych
     */
    void resendVerificationCode(VerificationDto verificationDto) throws GryfVerificationException;

    /**
     * Metoda która resetuje hasło dla użytkownika Usługodawcy. Tworzy link do zmiany hasła a następnie go wysyła na zadany adres o ile jest w bazie.
     * @param email - adres email osoby fizycznej
     */
    void resetTiUserPassword(String email);

}
