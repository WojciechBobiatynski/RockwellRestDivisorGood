package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform;

import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by krzysztof.krawczynski on 2018-03-26.
 */
@Getter
@Setter
public class TrainingInstanceExtDTO extends VersionableDto {

    private Long id;

    private Long trainingInstitution;

    private String vatRegNum;

    private String name;

    private String trainingExternalId; // externalId

    private Long trainingId;

    private String trainingName;

    private Date startDate;

    private Date endDate;

    private String place;

    private BigDecimal price;

    private Integer hoursNumber;

    private BigDecimal hourPrice;

    private String category;

    private String subcategory;

    private String isExam;

    private String certificate;

    private String certificateRemark;

    private String indOrderExternalId;

    private String status;

    private String isQualification;

    private String isOtherQualification;

    private String qualificationCode;

    private Date registrationDate;

    private Integer maxParticipantsCount;

    private String priceValidateType;

}
