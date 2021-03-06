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
    private String rmbsType;

    @Getter
    @Setter
    private String rmbsTypeCode;

    @Getter
    @Setter
    private Long trainingInstanceId;

    @Getter
    @Setter
    private String trainingName;

    @Getter
    @Setter
    private String trainingExternalId;

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

    @Getter
    @Setter
    private String rmbsStatusId;

    @Getter
    @Setter
    private String noteNumber;

    @Getter
    @Setter
    private String grantProgramName;

    @Setter
    @Getter
    private Long grantProgramId;
}
