package pl.sodexo.it.gryf.service.api.publicbenefits.contracts;

import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;

import java.util.List;

/**
 * Created by adziobek on 25.10.2016.
 */
public interface ContractService {

    List<GrantProgramDictionaryDTO> findGrantProgramsDictionaries();

    ContractDTO findContract(String id);

    List<ContractSearchResultDTO> findContracts(ContractSearchQueryDTO contractDto);

    ContractDTO createContract();

    String saveContract(ContractDTO contractDto);

    void updateContract(ContractDTO contractDto);

    List<DictionaryDTO> findContractTypesDictionaries();

    GrantProgramDictionaryDTO findGrantProgramOfFirstUserContract(String pesel);

    /**
     * Metoda dla procesu rezygnacji z umowy
     * @param contractId - id umowy w trakcie rezygnacji
     * @return - id zaktualizowanej umowy
     */
    String resign(String contractId);
}