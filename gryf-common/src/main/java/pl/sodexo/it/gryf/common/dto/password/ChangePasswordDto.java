package pl.sodexo.it.gryf.common.dto.password;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO dla zmiany hasła z poziomu FO
 *
 * Created by akmiecinski on 16.05.2017.
 */
@Data
public class ChangePasswordDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

}
