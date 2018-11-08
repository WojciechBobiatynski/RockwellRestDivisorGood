package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts;

import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;

import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
public interface ContractRepository extends GenericRepository<Contract, String> {

    List<Contract> findContracts(ContractSearchQueryDTO dto);

    Contract findFirstContractOfUser(String pesel);

    /**
     * Znajdz kontrakt dla danego progrmu
     * w okresie obowiazywania wyznaczonym
     * przez daty startDate i endDate
     *
     * @param grantProgramDictionaryDTO
     * @param individualId
     * @param startDate
     * @param endDate
     * @return Kontrakt/Umowa dla danego programu aktualna na dzień startDates
     */
    Contract findContractIndividualByProgramAndDate(GrantProgramDictionaryDTO grantProgramDictionaryDTO, Long individualId, Date startDate, Date endDate);
}
