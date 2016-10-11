package pl.sodexo.it.gryf.web.controller.password;

import static pl.sodexo.it.gryf.common.utils.StringUtils.isEmpty;

/**
 * Created by adziobek on 10.10.2016.
 *
 * Klasa sprawdzajaca poprawnosc hasla.
 */
public class PasswordValidator {

    private static final String PATTERN ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";

    public static boolean isValidNewPassword(PasswordData passwordData) {
        String oldPassword = passwordData.getOldPassword();
        String newPassword = passwordData.getNewPassword();
        String newPasswordRepeated = passwordData.getNewPasswordRepeated();

        if (isEmpty(oldPassword) || isEmpty(newPassword) || isEmpty(newPasswordRepeated)) {
            return false;
        }
        if (!newPassword.equals(newPasswordRepeated)) {
            return false;
        }
        if (oldPassword.equals(newPassword)) {
            return false;
        }
        if (!(validate(oldPassword) && validate(newPassword))) {
            return false;
        }
        return true;
    }

    private static boolean validate(String password) {
        if (password.matches(PATTERN)) {return true;}
        return false;
    }
}