package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
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
    IndividualDto findIndividualAfterLogin();
}
