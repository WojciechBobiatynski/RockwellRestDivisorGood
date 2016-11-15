package pl.sodexo.it.gryf.common.criteria.electronicreimbursements;

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
 * Kryteria wyszukiwania dla listy rozliczeń elektronicznych
 *
 * Created by akmiecinski on 14.11.2016.
 */
@ToString
public class ElctRmbsCriteria extends UserCriteria {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long rmbsNumber;

    @Getter
    @Setter
    private String trainingName;

    @Getter
    @Setter
    private String pesel;

    @Getter
    @Setter
    private String participantName;

    @Getter
    @Setter
    private String participantSurname;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date rmbsDateFrom;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date rmbsDateTo;

    @Getter
    @Setter
    private String rmbsStatus;

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
