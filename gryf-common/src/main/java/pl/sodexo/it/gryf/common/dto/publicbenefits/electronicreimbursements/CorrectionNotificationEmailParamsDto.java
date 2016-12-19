package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Dto z parametrami potrzebnymi do wysyłki maili z informacją o korekcie
 *
 * Created by akmiecinski on 19.12.2016.
 */
@ToString
public class CorrectionNotificationEmailParamsDto {

    @Getter
    @Setter
    private Long rmbsNumber;

    @Getter
    @Setter
    private String grantProgramName;

    @Getter
    @Setter
    private List<String> emails;

    @Getter
    @Setter
    private Date arrivalDate;

}
