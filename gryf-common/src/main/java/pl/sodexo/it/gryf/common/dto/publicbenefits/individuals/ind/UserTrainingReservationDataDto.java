package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;

import java.util.List;

/**
 * Dane wymagane przy rejestracji użytkownika na szkolenie.
 */
public class UserTrainingReservationDataDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String pesel;

    @Getter
    @Setter
    private List<GrantProgramDictionaryDTO> grantPrograms;

    @Getter
    @Setter
    private List<PbeProductInstancePoolDto> productInstancePools;
}