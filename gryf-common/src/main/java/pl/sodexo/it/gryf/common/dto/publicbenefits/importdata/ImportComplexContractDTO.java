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
    private ImportContractDTO contract = new ImportContractDTO();

    @Getter
    @Setter
    private ImportIndividualDTO individual = new ImportIndividualDTO();

    @Getter
    @Setter
    private ImportEnterpriseDTO enterprise = new ImportEnterpriseDTO();

    //PUBLIC METHODS

    public boolean checkContractType(String type) {
        if(type != null) {
            return type.equals(contract.getContractType());
        }
        return false;
    }
}
