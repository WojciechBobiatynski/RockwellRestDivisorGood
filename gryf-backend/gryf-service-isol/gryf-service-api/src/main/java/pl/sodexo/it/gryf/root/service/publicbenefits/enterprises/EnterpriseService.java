package pl.sodexo.it.gryf.root.service.publicbenefits.enterprises;

import pl.sodexo.it.gryf.dto.publicbenefits.enterprises.searchform.EnterpriseSearchQueryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;

import java.util.List;
import java.util.Set;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface EnterpriseService {

    Enterprise findEnterprise(Long id);

    List<EnterpriseSearchResultDTO> findEnterprises(EnterpriseSearchQueryDTO enterprise);

    Enterprise createEnterprise();

    Enterprise saveEnterprise(Enterprise enterprise, boolean checkVatRegNumDup);

    /**
     * Funkcja zwraca zbiór adresów e-mail odbiorców przypisanych do przekazanego Przedsiębiorstwa. Tak zwrócony zbiór można sformatować np przy pomocy funkcji
     * {@link pl.sodexo.it.gryf.utils.GryfUtils#formatEmailRecipientsSet GryfUtils.formatEmailRecipientsSet}
     * @param enterprise przekazany obiekt przedsiębiorstwa
     * @param existingRecipientsSet istniejący wcześniej zbiór kontaktów email do uzupełnienia (może być null wtedy zostanie stworzony i zwrócony nowy zbiór)
     * @return Zbiór uzupełniony kontaktami przekazanego przedsiębiorstwa
     */
    Set<String> getEmailRecipients(Enterprise enterprise, Set<String> existingRecipientsSet);

    void updateEnterprise(Enterprise enterprise, boolean checkVatRegNumDup);
}
