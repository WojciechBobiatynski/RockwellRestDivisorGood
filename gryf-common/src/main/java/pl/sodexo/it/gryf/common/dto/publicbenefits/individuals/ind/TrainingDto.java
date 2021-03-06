package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.util.Date;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla usługi.
 */
public class TrainingDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String trainingInstitutionName;

    @Getter
    @Setter
    private Integer productsCount;

    @Getter
    @Setter
    private Date signDate;

    @Getter
    @Setter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;

    @Getter
    @Setter
    String trainingStatus;

    @Getter
    @Setter
    String trainingStatusId;


    @Getter
    @Setter
    private String grantProgramName;

    @Setter
    @Getter
    private Long grantProgramId;
}