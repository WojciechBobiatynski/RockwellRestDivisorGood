package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.IndDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;
import pl.sodexo.it.gryf.dao.api.search.dao.IndividualSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.IndividualSearchMapper;

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

        //w przyszłości użytkownik TI będzie miał możliwość wybrania grant programu powiązanego z umową osoby fizycznej
        //w tej chwili pobieramy tylko pierwszą umowę, bo jest tylko jeden grant program
        //(umowy posortowane po expiry_date desc wiec weźmiemy tą najbardziej aktualną)
        Optional<ContractSearchResultDTO> firstContract = reservationDataDto.getContracts().stream().findFirst();
        if(firstContract.isPresent()) {
            reservationDataDto.getContracts().clear();
            reservationDataDto.getContracts().add(firstContract.get());
        }
        return reservationDataDto;
    }

    @Override
    public IndDto findIndividualAfterLogin() {
        return individualSearchMapper.findIndividualAfterLogin(new UserCriteria());
    }
}
