package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Isolution on 2016-12-13.
 */
public class ImportResultDTO {

    @Getter
    @Setter
    private String descrption;

    @Getter
    @Setter
    private Long contractId;

    @Getter
    @Setter
    private Long individualId;

    @Getter
    @Setter
    private Long enterpriseId;

    @Getter
    @Setter
    private Long orderId;

    @Getter
    @Setter
    private Long trainingInstitutionId;

    @Getter
    @Setter
    private Long trainingId;

}
