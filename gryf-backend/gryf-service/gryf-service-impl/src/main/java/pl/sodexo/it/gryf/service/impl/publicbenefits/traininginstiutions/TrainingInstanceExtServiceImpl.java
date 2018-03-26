package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstanceExtDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryCatalogRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingCategoryRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceExtRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceExt;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceExtService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions.TrainingDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions.TrainingInstanceExtDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions.TraningCategoryCatalogEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.traininginstiutions.TraningCategoryEntityMapper;
import pl.sodexo.it.gryf.service.validation.publicbenefits.traininginstiutions.TrainingValidator;

/**
 * Created by krzysztof.krawczynski on 2018-03-26.
 */
@Service
@Transactional
public class TrainingInstanceExtServiceImpl implements TrainingInstanceExtService {
    @Autowired
    private TrainingSearchDao trainingSearchDao;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingInstanceExtRepository trainingInstanceExtRepository;

    @Autowired
    private TrainingDtoMapper trainingDtoMapper;

    @Autowired
    private TrainingInstanceExtDtoMapper trainingInstanceExtDtoMapper;

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

    @Override
    public TrainingInstanceExtDTO createTrainingInstanceExt() {
        return new TrainingInstanceExtDTO();
    }

    @Override
    public Long saveTrainingInstanceExt(TrainingInstanceExtDTO trainingInstanceExtDto, Long importJobId) {
        TrainingInstanceExt trainingInstanceExt = trainingInstanceExtDtoMapper.convert(trainingInstanceExtDto);
        trainingInstanceExt.setImportJobId(importJobId);
        return trainingInstanceExtRepository.save(trainingInstanceExt).getId();
    }

}
