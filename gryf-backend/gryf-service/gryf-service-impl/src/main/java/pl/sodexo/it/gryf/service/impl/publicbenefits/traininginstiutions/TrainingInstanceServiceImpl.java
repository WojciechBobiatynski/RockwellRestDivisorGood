package pl.sodexo.it.gryf.service.impl.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingInstanceSearchDao;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
@Service
@Transactional
public class TrainingInstanceServiceImpl implements TrainingInstanceService {

    @Autowired
    private TrainingInstanceSearchDao trainingInstanceSearchDao;

    @Override
    public List<TrainingInstanceDto> findTrainingInstanceListByCriteria(TrainingInstanceCriteria criteria) {
        return trainingInstanceSearchDao.findTrainingToReimburseListByCriteria(criteria);
    }

    @Override
    public TrainingInstanceDetailsDto findTrainingInstanceDetails(Long trainingInstanceId) {
        return trainingInstanceSearchDao.findTrainingInstanceDetails(trainingInstanceId);
    }

    @Override
    public List<SimpleDictionaryDto> findTrainingInstanceStatuses() {
        return trainingInstanceSearchDao.findTiTrainingInstancesStatuses();
    }
}
