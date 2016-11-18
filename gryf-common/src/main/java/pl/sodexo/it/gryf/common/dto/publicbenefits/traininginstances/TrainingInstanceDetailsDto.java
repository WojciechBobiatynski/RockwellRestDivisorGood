package pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ToString
public class TrainingInstanceDetailsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long trainingInstanceId;

    @Getter
    @Setter
    private Integer productAssignedNum;

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
}