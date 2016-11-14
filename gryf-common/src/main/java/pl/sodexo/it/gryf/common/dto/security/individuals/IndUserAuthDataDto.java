package pl.sodexo.it.gryf.common.dto.security.individuals;

/**
 * Dto przechowujące pesel i kod weryfikacyjny osoby fizycznej
 */
public class IndUserAuthDataDto {

    private String pesel;
    private String verificationCode;

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
