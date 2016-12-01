package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.util.Date;

/**
 * Dto dla korekty
 *
 * Created by akmiecinski on 30.11.2016.
 */
@ToString
public class CorrectionDto extends VersionableDto{

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private Integer correctionNumber;

    @Getter
    @Setter
    @NotEmpty(message = "Powód korekty nie może być pusty")
    private String correctionReason;

    @Getter
    @Setter
    private Date complementDate;

    @Getter
    @Setter
    private Date requiredDate;
}
