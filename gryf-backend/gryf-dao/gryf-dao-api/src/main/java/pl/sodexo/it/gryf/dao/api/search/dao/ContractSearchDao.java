package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;

import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
public interface ContractSearchDao {

    List<ContractSearchResultDTO> findContracts(ContractSearchQueryDTO dto);

    ContractDTO findContract(Long id);
}
