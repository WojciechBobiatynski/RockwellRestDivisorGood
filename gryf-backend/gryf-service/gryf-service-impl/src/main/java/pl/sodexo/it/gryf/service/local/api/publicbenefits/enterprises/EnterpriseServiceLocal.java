package pl.sodexo.it.gryf.service.local.api.publicbenefits.enterprises;

import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;

import java.util.Set;

/**
 * Serwis lokalny obsługujący przedsiebiorstwa
 * Created by jbentyn on 2016-09-27.
 */
public interface EnterpriseServiceLocal {

    Enterprise saveEnterprise(Enterprise enterprise, boolean checkVatRegNumDup, boolean validateAccountRepayment);

    /**
     * Funkcja zwraca zbiór adresów e-mail odbiorców przypisanych do przekazanego Przedsiębiorstwa. Tak zwrócony zbiór można sformatować np przy pomocy funkcji
     * {@link GryfUtils#formatEmailRecipientsSet GryfUtils.formatEmailRecipientsSet}
     *
     * @param enterprise przekazany obiekt przedsiębiorstwa
     * @param existingRecipientsSet istniejący wcześniej zbiór kontaktów email do uzupełnienia (może być null wtedy zostanie stworzony i zwrócony nowy zbiór)
     * @return Zbiór uzupełniony kontaktami przekazanego przedsiębiorstwa
     */
    Set<String> getEmailRecipients(Enterprise enterprise, Set<String> existingRecipientsSet);

    void updateEnterprise(Enterprise enterprise, boolean checkVatRegNumDup, boolean validateAccountRepayment);

}
