package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.*;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;
import pl.sodexo.it.gryf.dao.api.search.dao.IndividualSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.IndividualSearchMapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public UserTrainingReservationDataDto findDataForTrainingReservation(String pesel) {
        UserTrainingReservationDataDto reservationDataDto = individualSearchMapper.findDataForTrainingReservation(pesel);

        return reservationDataDto;
    }

    @Override
    public List<ProductDto> findProductInstancePoolsByIndividual() {
        return individualSearchMapper.findProductInstancePoolsByIndividual(new UserCriteria());
    }

    @Override
    public List<TrainingDto> findTrainingsByIndividual() {
        return individualSearchMapper.findTrainingsByIndividual(new UserCriteria());
    }

    @Override
    public IndividualWithContactDto findIndividualAfterLogin() {
        return individualSearchMapper.findIndividualAfterLogin(new UserCriteria());
    }
}
