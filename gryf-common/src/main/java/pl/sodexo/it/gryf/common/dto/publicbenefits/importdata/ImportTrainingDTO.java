package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
public class ImportTrainingDTO {

    @Getter
    @Setter
    private String vatRegNum;

    @Getter
    @Setter
    private String trainingInstanceName;

    @Getter
    @Setter
    private Long externalId;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;

    @Getter
    @Setter
    private String status;

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
    private String reimbursmentCondition;
}
