package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions.TrainingDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions.TraningCategoryEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.traininginstiutions.TrainingValidator;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingSearchDao trainingSearchDao;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingDtoMapper trainingDtoMapper;

    @Autowired
    private TrainingValidator trainingValidator;

    @Autowired
    private TrainingCategoryRepository trainingCategoryRepository;

    @Autowired
    private TraningCategoryEntityMapper traningCategoryEntityMapper;

    @Override
    public TrainingDTO findTraining(Long id) {
        return trainingSearchDao.findTraining(id);
    }

    @Override
    public List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto) {
        return trainingSearchDao.findTrainings(dto);
    }

    @Override
    public List<SimpleDictionaryDto> getTrainingCategoriesDict() {
        return trainingSearchDao.findTrainingCategories();
    }

    @Override
    public TrainingDTO createTraining() {
        return new TrainingDTO();
    }

    @Override
    public Long saveTraining(TrainingDTO trainingDto) {
        Training training = trainingDtoMapper.convert(trainingDto);
        trainingValidator.validateTraining(training);
        return trainingRepository.save(training).getId();
    }

    @Override
    public void updateTraining(TrainingDTO trainingDto) {
        Training training = trainingDtoMapper.convert(trainingDto);
        trainingValidator.validateTraining(training);
        trainingRepository.update(training, training.getId());
    }

    @Override
    public TrainingSearchResultDTO findTrainingOfInstitutionById(Long id) {
        return trainingSearchDao.findTrainingOfInstitutionById(id);
    }

    @Override
    public List<SimpleDictionaryDto> findTrainingCategoriesByGrantProgram(Long grantProgramId) {
        List<TrainingCategory> trainingCategories = trainingCategoryRepository.findByGrantProgram(grantProgramId);
        return traningCategoryEntityMapper.convert(trainingCategories);
    }
}
