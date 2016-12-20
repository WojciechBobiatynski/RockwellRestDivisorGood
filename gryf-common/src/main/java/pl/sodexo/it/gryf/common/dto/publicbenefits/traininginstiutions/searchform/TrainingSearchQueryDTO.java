package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;
import pl.sodexo.it.gryf.common.utils.GryfConstants;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    private Date startDateFrom;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date startDateTo;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date endDateFrom;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date endDateTo;

    @Getter
    @Setter
    private Long maxProductCount;

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
    private List<String> categoryCodes;

    @Getter
    @Setter
    private String categoryName;

    @Getter
    @Setter
    private Boolean active;

    public String getActiveStr(){
        if(active == null){
            return null;
        }
        return (active) ? GryfConstants.FLAG_TRUE : GryfConstants.FLAG_FALSE;
    }

}
