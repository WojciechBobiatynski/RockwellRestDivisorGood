package pl.sodexo.it.gryf.service.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementPatternDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementPatternParamRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementPatternRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPatternParam;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementPatternService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.reimbursement.detailsform.ReimbursementPatternEntityMapper;

import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-09.
 */
@Service
@Transactional
public class ReimbursementPatternServiceImpl implements ReimbursementPatternService {

    //FIELDS

    @Autowired
    private ReimbursementPatternRepository reimbursementPatternRepository;

    @Autowired
    private ReimbursementPatternParamRepository reimbursementPatternParamRepository;

    @Autowired
    private ReimbursementPatternEntityMapper reimbursementPatternEntityMapper;

    //PUBLIC METHODS

    @Override
    public ReimbursementPatternDTO findReimbursementPattern(Long id) {
        ReimbursementPattern pattern = reimbursementPatternRepository.get(id);
        String reimbursementDelay = findReimbursementPatternParam(pattern.getId(), ReimbursementPatternParam.REIMBDELAY);
        return reimbursementPatternEntityMapper.convert(pattern, Integer.valueOf(reimbursementDelay));
    }

    @Override
    public String findReimbursementPatternParam(Long reimbursementPatternId, String paramTypeId){
        List<ReimbursementPatternParam> params = reimbursementPatternParamRepository.findByReimbursementPatternParamInDate(
                                                                                reimbursementPatternId, paramTypeId, new Date());

        if(params.size() == 0){
            throw new RuntimeException(String.format("B????d konfiguracji - dla danego wzorzeca rozlicze?? [%s] nie " +
                                                "znaleziono parametru [%s] w danej dacie", reimbursementPatternId, paramTypeId));
        }
        if(params.size() > 1){
            throw new RuntimeException(String.format("Dla danego wzorzeca rozlicze?? [%s] znaleziono wiecej ni??e jeden parametr [%s] w danej dacie",
                                                                reimbursementPatternId, paramTypeId));
        }
        return params.get(0).getValue();
    }

}
