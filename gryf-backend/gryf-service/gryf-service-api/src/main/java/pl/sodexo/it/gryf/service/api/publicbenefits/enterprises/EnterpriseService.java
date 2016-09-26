package pl.sodexo.it.gryf.service.api.publicbenefits.enterprises;

import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;

import java.util.List;
import java.util.Set;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface EnterpriseService {

    EnterpriseDto findEnterprise(Long id);

    List<EnterpriseSearchResultDTO> findEnterprises(EnterpriseSearchQueryDTO enterprise);

    EnterpriseDto createEnterprise();

    EnterpriseDto saveEnterpriseDto(EnterpriseDto enterpriseDto, boolean checkVatRegNumDup);

    // TODO zmienic nazwe i przeniesc do serwisu lokalnego
    Enterprise saveEnterprise(Enterprise enterprise, boolean checkVatRegNumDup);

    /**
     * Funkcja zwraca zbiór adresów e-mail odbiorców przypisanych do przekazanego Przedsiębiorstwa. Tak zwrócony zbiór można sformatować np przy pomocy funkcji
     * {@link GryfUtils#formatEmailRecipientsSet GryfUtils.formatEmailRecipientsSet}
     * @param enterprise przekazany obiekt przedsiębiorstwa
     * @param existingRecipientsSet istniejący wcześniej zbiór kontaktów email do uzupełnienia (może być null wtedy zostanie stworzony i zwrócony nowy zbiór)
     * @return Zbiór uzupełniony kontaktami przekazanego przedsiębiorstwa
     */
    //TODO przenieść do serwisu lokalneo
    Set<String> getEmailRecipients(Enterprise enterprise, Set<String> existingRecipientsSet);

    void updateEnterpriseDto(EnterpriseDto enterpriseDto, boolean checkVatRegNumDup);
    //TODO przenieść do serwisu lokalnego
    void updateEnterprise(Enterprise enterprise, boolean checkVatRegNumDup);
}
