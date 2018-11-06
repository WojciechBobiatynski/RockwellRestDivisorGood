package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

public class TrainingSearchResultDTO {

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
    private Long productCount;

    @Getter
    @Setter
    private String place;

    @Getter
    @Setter
    private BigDecimal hoursNumber;

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

    @Getter
    @Setter
    private String grantProgramId;
}
