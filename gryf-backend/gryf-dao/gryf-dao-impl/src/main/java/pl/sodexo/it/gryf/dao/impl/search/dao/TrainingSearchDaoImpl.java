package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.dao.api.search.dao.TrainingSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.TrainingSearchMapper;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class TrainingSearchDaoImpl implements TrainingSearchDao {

    @Autowired
    private TrainingSearchMapper trainingSearchMapper;


    @Override
    public List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto) {
        return trainingSearchMapper.findTrainings(new UserCriteria(), dto);
    }

    @Override
    public List<SimpleDictionaryDto> findTrainingCategories() {
        return trainingSearchMapper.findTrainingCategories(new UserCriteria());
    }

    @Override
    public TrainingDTO findTraining(Long trainingId) {
        return trainingSearchMapper.findTraining(trainingId);
    }
}
