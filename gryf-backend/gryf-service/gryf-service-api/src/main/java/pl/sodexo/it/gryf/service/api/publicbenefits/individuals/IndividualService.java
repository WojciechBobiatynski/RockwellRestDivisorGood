package pl.sodexo.it.gryf.service.api.publicbenefits.individuals;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;

import java.util.List;
import java.util.Set;

/**
 * Serwis do obdługi osób fizycznych
 *
 * Created by jbentyn on 2016-09-20.
 */
public interface IndividualService {

    IndividualDto findIndividual(Long id);

    IndividualDto findIndividualAfterLogin();

    IndividualDto findIndividualByPesel(String pesel);

    List<IndividualSearchResultDTO> findIndividuals(IndividualSearchQueryDTO individual);

    IndividualDto createIndividual();

    Long saveIndividual(IndividualDto individualDto, boolean checkPeselDup);

    /**
     * Funkcja zwraca zbiór adresów e-mail odbiorców przypisanych do przekazanej Osoby fizycznej. Tak zwrócony zbiór można sformatować np przy pomocy funkcji
     * {@link GryfUtils#formatEmailRecipientsSet GryfUtils.formatEmailRecipientsSet}
     * @param individualDto przekazany obiekt osoby fizycznej
     * @param existingRecipientsSet istniejący wcześniej zbiór kontaktów email do uzupełnienia (może być null wtedy zostanie stworzony i zwrócony nowy zbiór)
     * @return Zbiór uzupełniony kontaktami przekazanej osoby fizycznej
     */
    Set<String> getEmailRecipients(IndividualDto individualDto, Set<String> existingRecipientsSet);

    void updateIndividual(IndividualDto individualDto, boolean checkPeselDup);

    /**
     * Wysyła maila z kodem weryfikacyjnym
     * @param individualDto - dto osoby fizycznej
     * @param appUrl - adres aplikacji na potrzeby wysyłki maila
     */
    void sendEmailNotification(IndividualDto individualDto, String appUrl);
}
