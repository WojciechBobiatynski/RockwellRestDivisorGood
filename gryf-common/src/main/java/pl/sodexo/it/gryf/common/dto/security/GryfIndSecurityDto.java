package pl.sodexo.it.gryf.common.dto.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Dto reprezentujące użytkownika osoby fizycznej na potrzeby autentykacji
 *
 * Created by akmiecinski on 19.10.2016.
 */
@ToString
public class GryfIndSecurityDto implements Serializable{

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String inuId;

    @Getter
    @Setter
    private String pesel;

    @Getter
    @Setter
    private String verificationCode;

    @Getter
    @Setter
    private boolean active;

}
