package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Dto dla korekty
 *
 * Created by akmiecinski on 30.11.2016.
 */
@ToString
public class CorrectionDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private String correctionReason;

}
