package pl.sodexo.it.gryf.common.dto.security;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Reprezentacja dto uzytkownika aplikacji GRYF.
 *
 * Created by adziobek on 28.09.2016.
 */
@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String login;

    private String name;

    private String surname;

    private Date lastLogin;

    private boolean active;
  }