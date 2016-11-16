package pl.sodexo.it.gryf.common.criteria.trainingtoreimburse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.enums.SortType;

import java.util.Date;
import java.util.List;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.DATE_FORMAT;

/**
 * Kryteria wyszukiwania dla szkoleń do rozliczenia
 *
 * Created by akmiecinski on 14.11.2016.
 */
@ToString
public class TrainingToReimburseCriteria extends UserCriteria {

    private static final long serialVersionUID = 1L;

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
