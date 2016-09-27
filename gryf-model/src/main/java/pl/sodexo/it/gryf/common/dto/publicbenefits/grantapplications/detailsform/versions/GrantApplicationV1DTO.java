package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.versions;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationV0BaseDTO;

/**
 * Created by tomasz.bilski.ext on 2015-06-25.
 */
@ToString
public class GrantApplicationV1DTO extends GrantApplicationV0BaseDTO {

    //FIELDS

    private String regon;

    private String enterpriseEstablishedYear;

    //GETTERS & SETTERS


    public String getRegon() {
        return regon;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }

    public String getEnterpriseEstablishedYear() {
        return enterpriseEstablishedYear;
    }

    public void setEnterpriseEstablishedYear(String enterpriseEstablishedYear) {
        this.enterpriseEstablishedYear = enterpriseEstablishedYear;
    }

}
