package pl.sodexo.it.gryf.common.dto.trainingtoreimburse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.enums.SortType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Kryteria wyszukiwania dla szkoleń do rozliczenia
 *
 * Created by akmiecinski on 14.11.2016.
 */
@ToString
public class TrainingToReimburseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long trainingInstanceId;

    @Getter
    @Setter
    private String trainingName;

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
    private String trainingStatus;

    @Getter
    @Setter
    private Integer limit;

    @Getter
    @Setter
    private List<String> sortColumns;

    @Getter
    @Setter
    protected List<SortType> sortTypes;
}
