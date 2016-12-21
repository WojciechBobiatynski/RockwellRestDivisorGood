package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsMailDto;

import java.util.List;

/**
 * Serwis do operacji na mailach rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 30.11.2016.
 */
public interface ErmbsMailService {

    List<ErmbsMailDto> createMailFromTemplates(Long ermbsId);

}
