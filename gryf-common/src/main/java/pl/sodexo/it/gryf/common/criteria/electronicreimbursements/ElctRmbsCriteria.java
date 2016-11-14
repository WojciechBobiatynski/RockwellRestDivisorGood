package pl.sodexo.it.gryf.common.criteria.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;

import java.util.Date;

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
    private Date rmbsDateFrom;

    @Getter
    @Setter
    private Date rmbsDateTo;

    @Getter
    @Setter
    private String rmbsStatus;
}
