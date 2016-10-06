package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementAttachmentDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.OrderElementService;

/**
 * Created by jbentyn on 2016-10-06.
 */
public interface OrderElementAttachmentService extends OrderElementService<OrderElementAttachmentDTO> {

    void updateValue(OrderElement element, String fileLocation);
}
