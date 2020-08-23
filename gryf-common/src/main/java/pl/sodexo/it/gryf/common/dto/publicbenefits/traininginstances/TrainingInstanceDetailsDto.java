package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ProductCalculationsDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ToString(exclude = "pinCode")
public class TrainingInstanceDetailsDto extends ProductCalculationsDto implements Serializable {

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
    private String trainingExternalId;

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
    private Long grantProgramId;

    @Getter
    @Setter
    private String grantProgramName;

    @Getter
    @Setter
    private String reimbursmentConditions;

    @Getter
    @Setter
    private String pinCode;

    @Getter
    @Setter
    private String indOrderExternalId;

    public Integer getMaxProductsNumber() {
        return super.getMaxProductsNumber(this.trainingPrice, this.trainingHoursNumber);
    }
}
