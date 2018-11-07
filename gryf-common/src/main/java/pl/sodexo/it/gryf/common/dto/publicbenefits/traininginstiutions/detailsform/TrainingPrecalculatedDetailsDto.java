package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ProductCalculationsDto;

import java.math.BigDecimal;
import java.util.Date;

public class TrainingPrecalculatedDetailsDto extends ProductCalculationsDto {

    @Getter
    @Setter
    private Long trainingId;

    @Getter
    @Setter
    private Long institutionId;

    @Getter
    @Setter
    private String institutionName;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String externalId;

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
    private Boolean active;

    @Getter
    @Setter
    private Integer version;

    @Getter
    @Setter
    private String grantProgramName;

    @Setter
    @Getter
    private Long grantProgramId;

    public Integer getMaxProductsNumber() {
        return super.getMaxProductsNumber(this.price, this.hoursNumber);
    }
}
