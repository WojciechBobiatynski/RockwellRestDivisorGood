package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform;

import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.validation.publicbenefits.grantapplication.ValidationGroupApplyOptionalApplication;

import javax.validation.constraints.Size;

/**
 * Created by tomasz.bilski.ext on 2015-07-06.
 */
public class GrantApplicationPkdDTO {

    //FIELDS

    private Long id;

    @NotEmpty(message = "Kod nie może być pusty", groups = ValidationGroupApplyOptionalApplication.class)
    @Size(message = "Kod może mieć maksymalnie 10 znaków", max = 10)
    private String name;

    //GETTERS & SETETRS

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

}
