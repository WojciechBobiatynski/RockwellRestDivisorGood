package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;
import pl.sodexo.it.gryf.dao.api.search.dao.ContractSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.ContractSearchMapper;

import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ContractSearchDaoImpl implements ContractSearchDao {

    @Autowired
    private ContractSearchMapper contractSearchMapper;

    @Override
    public List<ContractSearchResultDTO> findContracts(ContractSearchQueryDTO dto) {
        //return contractSearchMapper.findContracts(dto);
        return null;
    }

    @Override
    public ContractDTO findContract(Long id) {
        //return contractSearchMapper.findContract(id);
        return null;
    }
}
