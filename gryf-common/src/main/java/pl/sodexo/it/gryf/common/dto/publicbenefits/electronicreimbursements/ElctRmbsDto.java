package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto dla listy rozliczeń elektronicznych
 *
 * Created by akmiecinski on 14.11.2016.
 */
@ToString
public class ElctRmbsDto implements Serializable {

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
    private Date rmbsDate;

    @Getter
    @Setter
    private String rmbsStatus;
}
