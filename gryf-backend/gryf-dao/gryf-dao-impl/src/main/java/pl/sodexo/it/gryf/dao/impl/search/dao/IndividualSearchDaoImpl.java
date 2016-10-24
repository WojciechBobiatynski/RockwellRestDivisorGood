package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;
import pl.sodexo.it.gryf.dao.api.search.dao.IndividualSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.IndividualSearchMapper;

/**
 * Implementacja DAO związanego z osobami fizycznymi
 *
 * Created by akmiecinski on 18.10.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class IndividualSearchDaoImpl implements IndividualSearchDao {

    @Autowired
    private IndividualSearchMapper individualSearchMapper;

    @Override
    public Long findIndividualIdByPeselAndEmail(VerificationDto verificationDto) {
        return individualSearchMapper.findIndividualIdByPeselAndEmail(new UserCriteria(), verificationDto);
    }
}
