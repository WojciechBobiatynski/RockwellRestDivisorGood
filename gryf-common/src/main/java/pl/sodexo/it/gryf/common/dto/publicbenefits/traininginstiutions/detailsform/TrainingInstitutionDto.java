package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-27.
 */
@ToString
public class TrainingInstitutionDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String externalId;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String vatRegNum;

    @Getter
    @Setter
    private String addressInvoice;

    @Getter
    @Setter
    private ZipCodeDto zipCodeInvoice;

    @Getter
    @Setter
    private String addressCorr;

    @Getter
    @Setter
    private ZipCodeDto zipCodeCorr;

    @Getter
    @Setter
    private String remarks;

    @Getter
    @Setter
    private List<TrainingInstitutionContactDto> contacts;

    @Getter
    @Setter
    private List<GryfTiUserDto> users;

}
