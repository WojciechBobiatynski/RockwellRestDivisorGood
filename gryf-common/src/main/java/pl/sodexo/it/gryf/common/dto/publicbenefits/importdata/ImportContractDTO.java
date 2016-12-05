package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by Isolution on 2016-11-30.
 */
@ToString
public class ImportContractDTO {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long grantProgramId;

    @Getter
    @Setter
    private String contractType;

    @Getter
    @Setter
    private Date signDate;

    @Getter
    @Setter
    private Date expiryDate;

    @Getter
    @Setter
    private String contractTrainingCategories;

}
