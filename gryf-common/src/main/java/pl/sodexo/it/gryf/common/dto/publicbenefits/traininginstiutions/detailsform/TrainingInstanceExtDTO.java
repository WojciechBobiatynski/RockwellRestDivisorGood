package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform;

import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by krzysztof.krawczynski on 2018-03-26.
 */
public class TrainingInstanceExtDTO extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long trainingInstitution;

    @Getter
    @Setter
    private String vatRegNum;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String trainingExternalId; // externalId

    @Getter
    @Setter
    private Long trainingId;


    @Getter
    @Setter
    private String trainingName;

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
    private BigDecimal price;

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
    private String certificateRemark;

    @Getter
    @Setter
    private String indOrderExternalId;

}
