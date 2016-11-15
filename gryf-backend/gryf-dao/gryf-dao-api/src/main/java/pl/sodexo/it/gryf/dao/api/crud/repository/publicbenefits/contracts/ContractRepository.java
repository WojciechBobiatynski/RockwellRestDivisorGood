package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts;

import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;

import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
public interface ContractRepository extends GenericRepository<Contract, Long> {

    List<Contract> findContracts(ContractSearchQueryDTO dto);

    Contract findFirstContractOfUser(String pesel);
}
