package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2016-10-26.
 */
public class TrainingDTO extends VersionableDto {

    @Getter
    @Setter
    private Long trainingId;

    @Getter
    @Setter
    private String externalTrainingId;

    @Getter
    @Setter
    private Long trainingInstitution;

    @Getter
    @Setter
    private String institutionName;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private BigDecimal price;

    @Getter
    @Setter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;

    @Getter
    @Setter
    private String place;

    @Getter
    @Setter
    private Integer hoursNumber;

    @Getter
    @Setter
    private BigDecimal hourPrice;

    @Getter
    @Setter
    private String category;

    @Getter
    @Setter
    private String trainingCategoryCatalogId;

    @Getter
    @Setter
    private String reimbursmentConditions;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private String deactivateUser;

    @Getter
    @Setter
    private Date deactivateDate;

    @Getter
    @Setter
    private Long deactivateJobId;
}
