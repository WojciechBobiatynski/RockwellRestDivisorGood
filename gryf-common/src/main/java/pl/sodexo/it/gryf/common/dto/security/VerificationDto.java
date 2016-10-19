package pl.sodexo.it.gryf.common.dto.security;

import java.io.Serializable;

/**
 * Created by akmiecinski on 17.10.2016.
 */
public class VerificationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pesel;
    private String email;

    public VerificationDto(String pesel, String email){
        this.pesel = pesel;
        this.email = email;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "VerificationDto{" + "pesel='" + pesel + '\'' + ", email='" + email + '\'' + '}';
    }
}
