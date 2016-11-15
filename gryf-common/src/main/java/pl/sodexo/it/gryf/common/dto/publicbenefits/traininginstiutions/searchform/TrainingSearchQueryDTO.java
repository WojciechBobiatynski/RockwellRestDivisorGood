package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

import java.math.BigDecimal;
import java.util.Date;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.DATE_FORMAT;

@ToString
public class TrainingSearchQueryDTO extends SearchDto {

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
    private BigDecimal priceFrom;

    @Getter
    @Setter
    private BigDecimal priceTo;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date startDate;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date endDate;

    @Getter
    @Setter
    private String place;

    @Getter
    @Setter
    private BigDecimal hoursNumberFrom;

    @Getter
    @Setter
    private BigDecimal hoursNumberTo;

    @Getter
    @Setter
    private BigDecimal hourPriceFrom;

    @Getter
    @Setter
    private BigDecimal hourPriceTo;

    @Getter
    @Setter
    private String categoryCode;
}
