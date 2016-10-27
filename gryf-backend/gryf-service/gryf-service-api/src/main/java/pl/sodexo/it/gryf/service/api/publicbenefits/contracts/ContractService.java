package pl.sodexo.it.gryf.service.api.publicbenefits.contracts;

import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;

import java.util.List;

/**
 * Created by adziobek on 25.10.2016.
 */
public interface ContractService {

    List<GrantProgramDictionaryDTO> FindGrantProgramsDictionaries();

    ContractDTO findContract(Long id);

    List<ContractSearchResultDTO> findContracts(ContractSearchQueryDTO contractDto);

    ContractDTO createContract();

    Long saveContract(ContractDTO contractDto);

    void updateContract(ContractDTO contractDto);
}