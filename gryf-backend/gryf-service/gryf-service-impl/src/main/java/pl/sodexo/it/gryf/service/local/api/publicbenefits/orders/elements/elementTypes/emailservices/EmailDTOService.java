package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.emailservices;

import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;

/**
 *
 * @author Marcel.GOLUNSKI
 */
public interface EmailDTOService {
    MailDTO createMailDTO(OrderElementDTOBuilder builder);
}
