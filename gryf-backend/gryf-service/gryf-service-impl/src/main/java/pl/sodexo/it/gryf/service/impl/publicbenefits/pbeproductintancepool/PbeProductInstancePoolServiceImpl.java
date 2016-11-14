package pl.sodexo.it.gryf.service.impl.publicbenefits.pbeproductintancepool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.IndUserAuthDataDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.pbeproducts.PbeProductInstancePoolRepository;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePool;
import pl.sodexo.it.gryf.service.api.publicbenefits.individuals.IndividualService;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.trainingreservation.TrainingReservationValidator;

import java.util.List;

/**
 * Created by Isolution on 2016-11-07.
 */
@Service
@Transactional
public class PbeProductInstancePoolServiceImpl implements PbeProductInstancePoolService {

    @Autowired
    private PbeProductInstancePoolEntityMapper productInstancePoolEntityMapper;

    @Autowired
    private PbeProductInstancePoolRepository productInstancePoolRepository;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private TrainingReservationValidator trainingReservationValidator;

    @Override
    public List<PbeProductInstancePoolDto> findProductInstancePoolsOfUser(IndUserAuthDataDto userAuthDataDto) {
        IndividualDto individualDto = individualService.findIndividualByPesel(userAuthDataDto.getPesel());
        trainingReservationValidator.validateIndUserAuthorizationData(userAuthDataDto, individualDto);

        List<PbeProductInstancePool> productInstancePools = productInstancePoolRepository.findByIndividualUser(individualDto.getId());
        return productInstancePoolEntityMapper.convert(productInstancePools);
    }
}
