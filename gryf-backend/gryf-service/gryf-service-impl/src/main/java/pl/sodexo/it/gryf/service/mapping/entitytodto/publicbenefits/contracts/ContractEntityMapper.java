package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.ZipCodeEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.enterprises.searchform.EnterpriseEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.grantprograms.GrantProgramEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.individuals.searchform.IndividualEntityToSearchResultMapper;

import java.util.ArrayList;

/**
 * Maper mapujący Contract na ContractDto
 *
 * Created by adziobek on 04.11.2016.
 */
@Component
public class ContractEntityMapper extends VersionableEntityMapper<Contract, ContractDTO> {

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;
    @Autowired
    private GrantProgramEntityMapper grantProgramEntityMapper;

    @Autowired
    private IndividualEntityToSearchResultMapper individualEntityToSearchResultMapper;

    @Autowired
    private EnterpriseEntityToSearchResultMapper enterpriseEntityToSearchResultMapper;

    @Autowired
    private ZipCodeEntityMapper zipCodeEntityMapper;

    @Override
    protected ContractDTO initDestination() {
        return new ContractDTO();
    }

    public void map(Contract entity, ContractDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setContractType(dictionaryEntityMapper.convert(entity.getContractType()));
        dto.setGrantProgram(grantProgramEntityMapper.convert(entity.getGrantProgram()));
        if (entity.getIndividual() != null) {
            dto.setIndividual(individualEntityToSearchResultMapper.convert(entity.getIndividual()));
        }
        if (entity.getEnterprise() != null) {
            dto.setEnterprise(enterpriseEntityToSearchResultMapper.convert(entity.getEnterprise()));
        }
        dto.setTrainingCategory(new ArrayList<>());
        for(TrainingCategory category : entity.getCategories()){
            dto.getTrainingCategory().add(category.getId());
        }

        dto.setSignDate(entity.getSignDate());
        dto.setExpiryDate(entity.getExpiryDate());

        dto.setCode(entity.getCode());
        dto.setAccountPayment(entity.getAccountPayment());

        dto.setAddressInvoice(entity.getAddressInvoice());
        dto.setZipCodeInvoice(zipCodeEntityMapper.convert(entity.getZipCodeInvoice()));
        dto.setAddressCorr(entity.getAddressCorr());
        dto.setZipCodeCorr(zipCodeEntityMapper.convert(entity.getZipCodeCorr()));

    }
}