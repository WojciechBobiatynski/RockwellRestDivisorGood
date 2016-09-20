package pl.sodexo.it.gryf.root.service.publicbenefits.individuals;

import pl.sodexo.it.gryf.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

import java.util.List;
import java.util.Set;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface IndividualService {

    Individual findIndividual(Long id);

    List<IndividualSearchResultDTO> findIndividuals(IndividualSearchQueryDTO individual);

    Individual createIndividual();

    Individual saveIndividual(Individual individual, boolean checkPeselDup);

    /**
     * Funkcja zwraca zbiór adresów e-mail odbiorców przypisanych do przekazanej Osoby fizycznej. Tak zwrócony zbiór można sformatować np przy pomocy funkcji
     * {@link pl.sodexo.it.gryf.utils.GryfUtils#formatEmailRecipientsSet GryfUtils.formatEmailRecipientsSet}
     * @param individual przekazany obiekt osoby fizycznej
     * @param existingRecipientsSet istniejący wcześniej zbiór kontaktów email do uzupełnienia (może być null wtedy zostanie stworzony i zwrócony nowy zbiór)
     * @return Zbiór uzupełniony kontaktami przekazanej osoby fizycznej
     */
    Set<String> getEmailRecipients(Individual individual, Set<String> existingRecipientsSet);

    void updateIndividual(Individual individual, boolean checkPeselDup);
}
