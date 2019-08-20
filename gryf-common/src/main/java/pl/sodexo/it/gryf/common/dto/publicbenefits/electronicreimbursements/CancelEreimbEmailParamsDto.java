package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Dto z parametrami potrzebnymi do wysyłki maili z informacją o anulowaniu rozliczenia
 *
 * Created by Damian.PTASZYNSKI on 2019-08-20.
 */
@ToString
public class CancelEreimbEmailParamsDto {

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
