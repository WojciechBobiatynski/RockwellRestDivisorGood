package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.IndDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.UserTrainingReservationDataDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;

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
    IndDto findIndividualAfterLogin();

    /**
     * Wyszukuje dane osoby fizycznej niezbędne na formatce rezerwacji szkolenia
     * @param pesel - pesel osoby fizycznej
     * @return date osoby fizycznej niezbędne do rezerwacji szkolenia
     */
    UserTrainingReservationDataDto findDataForTrainingReservation(String pesel);
}
