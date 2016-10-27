package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingSearchResultDTO;
import pl.sodexo.it.gryf.dao.api.search.mapper.ProductSearchMapper;
import pl.sodexo.it.gryf.dao.api.search.mapper.TrainingSearchMapper;

import java.util.List;

/**
 * Created by Isolution on 2016-10-26.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class TrainingSearchDaoImpl {

    @Autowired
    private TrainingSearchMapper trainingSearchMapper;

    List<TrainingSearchResultDTO> findTrainings(TrainingSearchQueryDTO dto){
        //return trainingSearchMapper.findTrainings(dto);
        return null;
    }

}
