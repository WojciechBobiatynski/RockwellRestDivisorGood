package pl.sodexo.it.gryf.web.controller.password;

import lombok.Data;

/**
 * Created by adziobek on 07.10.2016.
 */
@Data
public class PasswordData {
    private String oldPassword;
    private String newPassword;
    private String newPasswordRepeated;
}