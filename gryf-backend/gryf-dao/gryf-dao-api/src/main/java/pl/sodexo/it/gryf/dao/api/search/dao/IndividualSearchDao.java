package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.*;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;

import java.util.List;

/**
 * Dao związane z osobą fizyczną
 *
 * Created by akmiecinski on 18.10.2016.
 */
public interface IndividualSearchDao {

    /**
     * Wyszukuje ID osoby fizycznej na podstawie numeru pesel i adresu email
     * @param verificationDto - obiekt z peselem i adresem email
     * @return - ID osoby fizycznej
     */
    Long findIndividualIdByPeselAndEmail(VerificationDto verificationDto);

    /**
     * Wyszukuje dane zalogowanego uczestnika
     * @return
     */
    IndividualWithContactDto findIndividualAfterLogin();

    /**
     * Wyszukuje dane osoby fizycznej niezbędne na formatce rezerwacji usługi
     * @param pesel - pesel osoby fizycznej
     * @return date osoby fizycznej niezbędne do rezerwacji usługi
     */
    UserTrainingReservationDataDto findDataForTrainingReservation(String pesel);

    /**
     * Wyszukuje instancje puli produktów dla danego Uczestnika
     *
     * @return Instancje Puli produktów
     */
    List<ProductDto> findProductInstancePoolsByIndividual();

    /**
     * Wyszukiwanie szkoleń dla danego uczestnika
     *
     * @return Szkolenia Uczestnika
     */
    List<TrainingDto> findTrainingsByIndividual();
}
