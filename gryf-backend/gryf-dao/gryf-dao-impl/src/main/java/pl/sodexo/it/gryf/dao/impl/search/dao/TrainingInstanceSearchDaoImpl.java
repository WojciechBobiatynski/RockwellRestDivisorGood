package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.criteria.traininginstance.TrainingInstanceCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDataToValidateDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDetailsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstances.TrainingInstanceDto;
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
    public List<TrainingInstanceDto> findTrainingToReimburseListByCriteria(TrainingInstanceCriteria criteria) {
        return trainingInstanceSearchMapper.findTrainingToReimburseListByCriteria(criteria);
    }

    @Override
    public TrainingInstanceDetailsDto findTrainingInstanceDetails(Long trainingInstanceId) {
        return trainingInstanceSearchMapper.findTrainingInstanceDetails(new UserCriteria(), trainingInstanceId);
    }

    @Override
    public TrainingInstanceDetailsDto findTrainingInstanceDetailsWithPinCode(Long trainingInstanceId) {
        return trainingInstanceSearchMapper.findTrainingInstanceDetailsWithPinCode(new UserCriteria(), trainingInstanceId);
    }

    @Override
    public List<SimpleDictionaryDto> findTiTrainingInstancesStatuses() {
        return trainingInstanceSearchMapper.findTiTrainingInstancesStatuses(new UserCriteria());
    }

    @Override
    public TrainingInstanceDataToValidateDto findTrainingInstanceDataToValidateReimbursementCreation(Long trainingInstanceId) {
        return trainingInstanceSearchMapper.findTrainingInstanceDataToValidateReimbursementCreation(new UserCriteria(), trainingInstanceId);
    }
}
