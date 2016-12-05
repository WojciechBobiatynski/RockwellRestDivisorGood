package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Isolution on 2016-12-01.
 */
@ToString
public class ImportComplexContractResultDTO {

    @Getter
    @Setter
    private Long contractId;

    @Getter
    @Setter
    private Long individualId;

    @Getter
    @Setter
    private Long enterpriseId;
}
