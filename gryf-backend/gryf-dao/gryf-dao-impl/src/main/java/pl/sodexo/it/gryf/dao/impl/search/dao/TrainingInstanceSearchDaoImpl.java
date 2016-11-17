package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstancesDto;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingInstanceSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.TrainingInstanceSearchMapper;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class TrainingInstanceSearchDaoImpl implements TrainingInstanceSearchDao {

    @Autowired
    private TrainingInstanceSearchMapper trainingInstanceSearchMapper;

    @Override
    public List<TrainingInstancesDto> findTrainingToReimburseListByCriteria(TrainingInstanceCriteria criteria) {
        return trainingInstanceSearchMapper.findTrainingToReimburseListByCriteria(criteria);
    }

    @Override
    public List<SimpleDictionaryDto> findTiTrainingInstancesStatuses() {
        return trainingInstanceSearchMapper.findTiTrainingInstancesStatuses(new UserCriteria());
    }
}
