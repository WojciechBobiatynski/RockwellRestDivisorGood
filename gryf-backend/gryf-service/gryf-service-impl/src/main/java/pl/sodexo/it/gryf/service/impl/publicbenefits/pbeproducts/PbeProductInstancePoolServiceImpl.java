package pl.sodexo.it.gryf.service.impl.publicbenefits.pbeproducts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.ContractPbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;
import pl.sodexo.it.gryf.dao.api.search.dao.ProductInstancePoolSearchDao;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.validation.publicbenefits.trainingreservation.TrainingReservationValidator;

import java.util.List;

/**
 * Created by Isolution on 2016-11-07.
 */
@Service
@Transactional
public class PbeProductInstancePoolServiceImpl implements PbeProductInstancePoolService {

    //PRIVATE FIELDS

    @Autowired
    private TrainingReservationValidator trainingReservationValidator;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private ProductInstancePoolSearchDao productInstancePoolSearchDao;

    //PUBLIC METHODS - FIND

    @Override
    public UserTrainingReservationDataDto findUserTrainingReservationData(IndUserAuthDataDto userAuthDataDto) {
        IndividualDto individualDto = individualService.findIndividualByPesel(userAuthDataDto.getPesel());
        trainingReservationValidator.validateIndUserAuthorizationData(userAuthDataDto, individualDto);

        return individualService.findUserTrainingReservationData(userAuthDataDto.getPesel());
    }

    @Override
    public List<ContractPbeProductInstancePoolDto> findPoolInstancesByContractId(String contractId) {
        return productInstancePoolSearchDao.findPoolInstancesByContractId(contractId);
    }

}
