package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;

/**
 * Created by Isolution on 2016-11-30.
 */
@ToString
public class ImportComplexContractDTO {

    @Getter
    @Setter
    @Valid
    private ImportContractDTO contract;

    @Getter
    @Setter
    @Valid
    private ImportIndividualDTO individual;

    @Getter
    @Setter
    private ImportEnterpriseDTO enterprise;
}
