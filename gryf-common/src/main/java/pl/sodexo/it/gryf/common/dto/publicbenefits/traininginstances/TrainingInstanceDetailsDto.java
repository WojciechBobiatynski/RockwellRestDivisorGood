package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.BIG_DECIMAL_INTEGER_SCALE;

@ToString
public class TrainingInstanceDetailsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long trainingInstanceId;

    @Getter
    @Setter
    private String trainingInstanceStatusId;

    @Getter
    @Setter
    private boolean opinionDone;

    @Getter
    @Setter
    private Long trainingId;

    @Getter
    @Setter
    private Long trainingInstitutionId;

    @Getter
    @Setter
    private String trainingInstitutionName;

    @Getter
    @Setter
    private String trainingInstitutionVatRegNum;

    @Getter
    @Setter
    private Integer productAssignedNum;

    @Getter
    @Setter
    private Integer trainingInstanceVersion;

    @Getter
    @Setter
    private String trainingName;

    @Getter
    @Setter
    private String trainingCategory;

    @Getter
    @Setter
    private String trainingPlace;

    @Getter
    @Setter
    private BigDecimal trainingPrice;

    @Getter
    @Setter
    private Integer trainingHoursNumber;

    @Getter
    @Setter
    private BigDecimal trainingHourPrice;

    @Getter
    @Setter
    private String participantId;

    @Getter
    @Setter
    private String participantPesel;

    @Getter
    @Setter
    private String participantName;

    @Getter
    @Setter
    private String participantSurname;

    @Getter
    @Setter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;

    @Getter
    @Setter
    private Integer productInstanceForHour;

    @Getter
    @Setter
    private Integer maxProductInstance;

    @Getter
    @Setter
    private BigDecimal prdValue;

    @Getter
    @Setter
    private Long grantProgramId;

    @Getter
    @Setter
    private String grantProgramName;

    @Getter
    @Setter
    private String reimbursmentConditions;

    public Integer getMaxProductsNumber() {
        Integer maxProductsNumber;
        if(maxProductInstance != null){
            if(isTrainingPriceLowerThanMaxProgramLimit()){
                BigDecimal result = trainingPrice.divide(prdValue, BIG_DECIMAL_INTEGER_SCALE, RoundingMode.UP);
                maxProductsNumber = result.intValue();
            } else {
                maxProductsNumber = maxProductInstance;
            }
        } else {
            maxProductsNumber = productInstanceForHour * trainingHoursNumber;
        }
        return maxProductsNumber;
    }

    private boolean isTrainingPriceLowerThanMaxProgramLimit() {
        return trainingPrice.compareTo(prdValue.multiply(BigDecimal.valueOf(maxProductInstance))) < 0;
    }
}
