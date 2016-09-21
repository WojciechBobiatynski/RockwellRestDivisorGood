package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.validation.publicbenefits.grantapplication.ValidationGroupApplyOptionalApplication;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by tomasz.bilski.ext on 2015-07-02.
 */
public class GrantApplicationContactDataDTO {

    //FIELDS

    private Long id;

    @NotEmpty(message = "Imię i nazwisko nie może być puste", groups = ValidationGroupApplyOptionalApplication.class)
    @Size(message = "Nazwa (imię i nazwisko) może mieć maksymalnie 100 znaków", max = 100)
    private String name;

    @Email(message = "Nieprawidłowy format email", groups = ValidationGroupApplyOptionalApplication.class)
    @Size(message = "Email może mieć maksymalnie 50 znaków", max = 50)
    private String email;

    @Pattern(message = "Numer telefonu musi zawierać tylko cyfry", regexp = "\\d+", groups = ValidationGroupApplyOptionalApplication.class)
    @Size(message = "Numer telefonu może mieć maksymalnie 20 znaków", max = 20)
    private String phone;

    //GETETRS & SETETRS


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
