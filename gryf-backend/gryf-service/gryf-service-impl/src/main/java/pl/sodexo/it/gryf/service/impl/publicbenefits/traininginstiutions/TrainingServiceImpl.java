package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.GenericBuilder;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.products.ProductCalculateDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingPrecalculatedDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingWithExternalIdSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryCatalogRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategoryCatalog;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingCategoryProdInsCalcTypeService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions.TrainingDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions.TraningCategoryCatalogEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions.TraningCategoryEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.traininginstiutions.TrainingValidator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Isolution on 2016-10-26.
 */
@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private ContractService contractService;

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
    private TrainingCategoryCatalogRepository trainingCategoryCatalogRepository;

    @Autowired
    private TraningCategoryEntityMapper traningCategoryEntityMapper;

    @Autowired
    private TraningCategoryCatalogEntityMapper traningCategoryCatalogEntityMapper;

    @Autowired
    private TrainingCategoryProdInsCalcTypeService trainingCategoryProdInsCalcTypeService;

    @Override
    public TrainingDTO findTraining(Long id) {
        return trainingSearchDao.findTraining(id);
    }

    @Override
    public List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto) {
        return trainingSearchDao.findTrainings(dto);
    }

    @Override
    public List<TrainingWithExternalIdSearchResultDTO> findTrainingsWithExternalId(TrainingSearchQueryDTO dto) {
        return trainingSearchDao.findTrainingsWithExternalId(dto);
    }

    @Override
    public List<SimpleDictionaryDto> findTrainingCategories() {
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
    public boolean isTrainingInLoggedUserInstitution(Long trainingId){
        return trainingRepository.isInUserInstitution(trainingId, GryfUser.getLoggedUserLogin());
    }

    @Override
    public TrainingSearchResultDTO findTrainingDetails(Long id) {
        return trainingSearchDao.findTrainingDetails(id);
    }

    @Override
    public TrainingPrecalculatedDetailsDto findTrainingPrecalculatedDetails(Long id, Long grantProgramId) {
        TrainingPrecalculatedDetailsDto trainingPrecalculatedDetailsDto = trainingSearchDao.findTrainingPrecalculatedDetails(id, grantProgramId);
        trainingPrecalculatedDetailsDto.setProductInstanceForHour(getCalculateProdForHour(id, grantProgramId));
        return trainingPrecalculatedDetailsDto;
    }

    @Override
    public List<SimpleDictionaryDto> findTrainingCategoriesByGrantProgram(Long grantProgramId) {
        List<TrainingCategory> trainingCategories = trainingCategoryRepository.findByGrantProgram(grantProgramId);
        return traningCategoryEntityMapper.convert(trainingCategories);
    }

    @Override
    public List<SimpleDictionaryDto> findTrainingCategoriesInCatalog(String catalogId) {
        List<TrainingCategory> trainingCategories = trainingCategoryRepository.findByCatalogId(catalogId);
        return traningCategoryEntityMapper.convert(trainingCategories);
    }

    @Override
    public List<TrainingSearchResultDTO> findTrainingsByProgramIdAndIndividualIdUsingContractsIds(TrainingSearchQueryDTO dto) {
        List<TrainingSearchResultDTO> results = new ArrayList<>();
        if (Objects.nonNull(dto)) {
            if (dto.getIndividualId() != null) {
                List<ContractDTO> individualOrders = contractService.findIndividualContracts(dto.getGrantProgramId(), dto.getIndividualId(), dto.getStartDateFrom());
                if(!individualOrders.isEmpty()) {
                    dto.setIndOrderExternalIds(individualOrders.stream().map(orderDTO -> orderDTO.getId()).collect(Collectors.toList()));
                    results = trainingSearchDao.findTrainings(dto);
                }
            }
        }
        return results;
    }

    @Override
    public List<SimpleDictionaryDto> findAllTrainingCategoryCatalogs() {
        List<TrainingCategoryCatalog> trainingCategoryCatalogs = trainingCategoryCatalogRepository.findAll();
        return traningCategoryCatalogEntityMapper.convert(trainingCategoryCatalogs);
    }

    /**
     * Wyliczenie liczby bonów rozliczających jedną godzinę usług szkoleniowych
     *
     * @param id id szkolenia
     * @param grantProgramId id programu
     * @return liczba bonów rozliczajca godzinę szkolenia
     */
    private Integer getCalculateProdForHour(Long id, Long grantProgramId) {
        Training training = trainingRepository.get(id);
        ProductCalculateDto productCalculateDto = GenericBuilder.of(ProductCalculateDto::new)
                .with(ProductCalculateDto::setCategoryId, training.getCategory().getId())
                .with(ProductCalculateDto::setGrantProgramId, grantProgramId)
                .with(ProductCalculateDto::setDate, new Date())
                .with(ProductCalculateDto::setTrainingId, id)
                .with(ProductCalculateDto::setMaxParticipants, training.getMaxParticipants())
                .build();
        return trainingCategoryProdInsCalcTypeService.getCalculateProductInstanceForHour(productCalculateDto);
    }
}
