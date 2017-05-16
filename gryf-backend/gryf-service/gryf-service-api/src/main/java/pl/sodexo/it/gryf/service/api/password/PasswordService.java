package pl.sodexo.it.gryf.service.api.password;

import pl.sodexo.it.gryf.common.dto.password.ChangePasswordDto;

/**
 * Interfejs realizujący operacje związane z hasłem dla FO
 *
 * Created by akmiecinski on 16.05.2017.
 */
public interface PasswordService {

    /**
     * Metoda zmieniająca hasło
     * @param changePasswordDto - obiekt z danymi dotyczącymi haseł
     */
    void changePassword(ChangePasswordDto changePasswordDto);
}
