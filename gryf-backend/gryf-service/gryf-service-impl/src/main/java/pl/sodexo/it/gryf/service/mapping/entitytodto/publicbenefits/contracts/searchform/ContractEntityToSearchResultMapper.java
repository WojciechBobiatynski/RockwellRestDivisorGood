package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.contracts.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.service.mapping.GenericMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.enterprises.searchform.EnterpriseEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.grantprograms.GrantProgramEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.individuals.searchform.IndividualEntityToSearchResultMapper;

/**
 * Klasa służy do mapowania encji Contract na ContractSearchResultDTO
 *
 * Created by adziobek on 02.11.2016.
 */
@Component
public class ContractEntityToSearchResultMapper extends GenericMapper<Contract, ContractSearchResultDTO> {

    @Autowired
    IndividualEntityToSearchResultMapper individualEntityToSearchResultMapper;

    @Autowired
    EnterpriseEntityToSearchResultMapper enterpriseEntityToSearchResultMapper;

    @Autowired
    GrantProgramEntityMapper grantProgramEntityMapper;

    @Autowired
    DictionaryEntityMapper dictionaryEntityMapper;

    @Override
    protected ContractSearchResultDTO initDestination() {
        return new ContractSearchResultDTO();
    }

    @Override
    protected void map(Contract entity, ContractSearchResultDTO dto) {
        dto.setContractId(entity.getId());
        dto.setSignDate(entity.getSignDate());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setContractType(dictionaryEntityMapper.convert(entity.getContractType()));
        dto.setGrantProgram(grantProgramEntityMapper.convert(entity.getGrantProgram()));
        dto.setIndividual(individualEntityToSearchResultMapper.convert(entity.getIndividual()));
        dto.setEnterprise(enterpriseEntityToSearchResultMapper.convert(entity.getEnterprise()));
    }
}