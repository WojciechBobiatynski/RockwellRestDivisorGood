package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Dto dla dodatkowych informacji rozliczenia
 */
@ToString
public class ErmbsAdditionalInformationDto implements Serializable{

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private String foComment;
}
