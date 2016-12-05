package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
public class ImportTrainingInstitutionDTO {

    @Getter
    @Setter
    private Long externalId;

    @Getter
    @Setter
    private String vatRegNum;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private ImportAddressDTO addressInvoice;

    @Getter
    @Setter
    private ImportAddressDTO addressCorr;

    @Getter
    @Setter
    private String email;
}
