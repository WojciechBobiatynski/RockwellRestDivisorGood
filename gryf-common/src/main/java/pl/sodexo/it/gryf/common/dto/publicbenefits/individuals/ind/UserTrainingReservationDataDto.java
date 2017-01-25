package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;

import java.util.List;

/**
 * Dane wymagane przy rejestracji użytkownika na usługa.
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
    private List<ContractSearchResultDTO> contracts;

    @Getter
    @Setter
    private List<PbeProductInstancePoolDto> productInstancePools;
}