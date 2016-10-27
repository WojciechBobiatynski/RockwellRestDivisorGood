package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingSearchDao;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;

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

    @Override
    public TrainingDTO findTraining(Long id) {
        //return trainingSearchDao.findTraining(id);
        return null;
    }

    @Override
    public List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto) {
        return trainingSearchDao.findTrainings(dto);
    }

    @Override
    public TrainingDTO createTraining() {
        return null;
    }

    @Override
    public Long saveTraining(TrainingDTO trainingDto) {
        return null;
    }

    @Override
    public void updateTraining(TrainingDTO trainingDto) {
    }
}
