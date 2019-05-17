package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2016-10-26.
 */
@Getter
@Setter
public class TrainingDTO extends VersionableDto {

    private Long trainingId;

    private String externalTrainingId;

    private Long trainingInstitution;

    private String institutionName;

    private String name;

    private BigDecimal price;

    private Date startDate;

    private Date endDate;

    private String place;

    private Integer hoursNumber;

    private BigDecimal hourPrice;

    private String category;

    private String trainingCategoryCatalogId;

    private String reimbursmentConditions;

    private boolean active;

    private String deactivateUser;

    private Date deactivateDate;

    private Long deactivateJobId;

    private Long grantProgramId;

    private boolean isIndividual;
}
