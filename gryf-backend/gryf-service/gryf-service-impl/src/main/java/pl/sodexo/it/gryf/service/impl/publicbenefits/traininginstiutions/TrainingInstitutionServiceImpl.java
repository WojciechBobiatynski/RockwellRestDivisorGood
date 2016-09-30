package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.api.other.ApplicationParameters;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstitutionService;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.traininginstiutions.TrainingInstitutionDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions.TrainingInstitutionEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.traininginstiutions.TrainingInstitutionValidator;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-10.
 */
@Service
@Transactional
public class TrainingInstitutionServiceImpl implements TrainingInstitutionService {

    //FIELDS

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private TrainingInstitutionEntityMapper trainingInstitutionEntityMapper;

    @Autowired
    private TrainingInstitutionDtoMapper trainingInstitutionDtoMapper;

    @Autowired
    private TrainingInstitutionEntityToSearchResultMapper trainingInstitutionEntityToSearchResultMapper;

    @Autowired
    private TrainingInstitutionValidator trainingInstitutionValidator;
    //PUBLIC METHODS

    @Override
    public TrainingInstitutionDto findTrainingInstitution(Long id) {
        return trainingInstitutionEntityMapper.convert(trainingInstitutionRepository.getForUpdate(id));
    }

    @Override
    public List<TrainingInstitutionSearchResultDTO> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO trainingInstitution) {
        List<TrainingInstitution> trainingInstitutions = trainingInstitutionRepository.findTrainingInstitutions(trainingInstitution);
        return trainingInstitutionEntityToSearchResultMapper.convert(trainingInstitutions);
    }

    @Override
    public TrainingInstitutionDto createTrainingInstitution() {
        return new TrainingInstitutionDto();
    }

    @Override
    public TrainingInstitutionDto saveTrainingInstitution(TrainingInstitutionDto trainingInstitutionDto, boolean checkVatRegNumDup) {
        TrainingInstitution trainingInstitution = trainingInstitutionDtoMapper.convert(trainingInstitutionDto);
        trainingInstitutionValidator.validateTrainingInstitution(trainingInstitution, checkVatRegNumDup);
        trainingInstitution = trainingInstitutionRepository.save(trainingInstitution);

        trainingInstitution.setCode(generateCode(trainingInstitution.getId()));
        trainingInstitutionRepository.update(trainingInstitution, trainingInstitution.getId());
        return trainingInstitutionEntityMapper.convert(trainingInstitution);
    }

    @Override
    public void updateTrainingInstitution(TrainingInstitutionDto trainingInstitutionDto, boolean checkVatRegNumDup) {
        TrainingInstitution trainingInstitution = trainingInstitutionDtoMapper.convert(trainingInstitutionDto);
        trainingInstitutionValidator.validateTrainingInstitution(trainingInstitution, checkVatRegNumDup);
        trainingInstitutionRepository.update(trainingInstitution, trainingInstitution.getId());
    }

    //PRIVATE METHODS


    private String generateCode(Long id){
        String prefix = applicationParameters.getGryfTrainingInstitutionCodePrefix();
        int zeroCount = applicationParameters.getGryfTrainingInstitutionCodeZeroCount();
        return String.format("%s%0" + zeroCount + "d",prefix, id);
    }



}
