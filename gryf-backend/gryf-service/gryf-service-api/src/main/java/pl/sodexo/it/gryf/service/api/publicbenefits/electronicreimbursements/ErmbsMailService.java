package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;

import java.util.List;

/**
 * Serwis do operacji na mailach rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 30.11.2016.
 */
public interface ErmbsMailService {

    /**
     * Tworzy maile zgodne z szablonami w momencie gdy jeszcze żaden mail nie został wysłany
     * @param ermbsId - id rozliczenia
     * @return lista maili z szablonu
     */
    List<ErmbsMailDto> createMailFromTemplates(Long ermbsId);

    /**
     * Wysyła maile z rozliczeń bonów elektronicznych
     * @param dto - dto maila rozliczeń
     * @return nowe dto
     */
    ErmbsMailDto sendErmbsMail(ErmbsMailDto dto);
}
