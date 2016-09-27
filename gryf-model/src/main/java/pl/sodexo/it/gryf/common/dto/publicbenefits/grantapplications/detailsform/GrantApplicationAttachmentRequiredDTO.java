package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform;

import lombok.ToString;

/**
 * Created by tomasz.bilski.ext on 2015-07-13.
 */
@ToString
public class GrantApplicationAttachmentRequiredDTO {

    //FIELDS

    private String name;

    private String remarks;

    //GETERS & SETERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
