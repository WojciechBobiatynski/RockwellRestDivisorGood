package pl.sodexo.it.gryf.common.criteria.traininginstance;

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
 * Kryteria wyszukiwania dla instancji usług
 *
 * Created by akmiecinski on 14.11.2016.
 */
@ToString
public class TrainingInstanceCriteria extends UserCriteria {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long trainingInstanceId;

    @Getter
    @Setter
    private Long trainingInstitutionId;

    @Getter
    @Setter
    private String trainingInstitutionVatRegNum;

    @Getter
    @Setter
    private String trainingInstitutionName;


    @Getter
    @Setter
    private Long trainingId;

    @Getter
    @Setter
    private String trainingName;

    @Getter
    @Setter
    private String trainingExternalId;

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
    private String trainingStatusId;

    @Getter
    @Setter
    private Integer limit;

    @Getter
    @Setter
    private List<String> sortColumns;

    @Getter
    @Setter
    protected List<SortType> sortTypes;

    @Getter
    @Setter
    private String grantProgramName;

    @Setter
    @Getter
    private Long grantProgramId;
}
