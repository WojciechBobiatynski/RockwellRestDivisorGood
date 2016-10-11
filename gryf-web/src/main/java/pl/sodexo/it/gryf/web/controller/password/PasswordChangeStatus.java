package pl.sodexo.it.gryf.web.controller.password;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by adziobek on 11.10.2016.
 *
 * Informacja o tym czy udalo sie zmienic haslo.
 */
@Data
@AllArgsConstructor
public class PasswordChangeStatus {
    private String status;
    private String message;
}