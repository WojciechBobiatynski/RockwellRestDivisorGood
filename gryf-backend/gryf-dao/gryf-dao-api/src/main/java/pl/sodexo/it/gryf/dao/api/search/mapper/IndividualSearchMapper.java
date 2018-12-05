package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.*;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;

import java.util.List;

/**
 * Mapper do wyszukiwania wyników związanych z osobą fizyczną
 *
 * Created by akmiecinski on 18.10.2016.
 */
public interface IndividualSearchMapper {

    /**
     * Wyszukuje ID osoby fizycznej na podstawie numeru pesel i adresu email
     * @param verificationDto - obiekt z peselem i adresem email
     * @return - ID osoby fizycznej
     */
    Long findIndividualIdByPeselAndEmail(@Param("criteria") UserCriteria criteria, @Param("verification")VerificationDto verificationDto);

    IndividualWithContactDto findIndividualAfterLogin(@Param("criteria") UserCriteria criteria);

    UserTrainingReservationDataDto findDataForTrainingReservation(@Param("pesel") String pesel);

    /**
     * Wyszukuje instancje puli produktów dla danego Uczestnika
     *
     * @param userCriteria
     * @return Instancje Puli produktów
     */
    List<ProductDto> findProductInstancePoolsByIndividual(@Param("criteria") UserCriteria userCriteria);


    /**
     * Wyszukiwanie szkoleń dla danego uczestnika
     *
     * @return Szkolenia Uczestnika
     */
    List<TrainingDto> findTrainingsByIndividual(@Param("criteria") UserCriteria userCriteria);
}
