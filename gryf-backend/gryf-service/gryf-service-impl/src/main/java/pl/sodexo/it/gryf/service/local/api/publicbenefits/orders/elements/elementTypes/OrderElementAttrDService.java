package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementAttrDDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.OrderElementService;

import java.util.Date;

/**
 * Created by jbentyn on 2016-10-06.
 */
public interface OrderElementAttrDService extends OrderElementService<OrderElementAttrDDTO> {

    void updateValue(OrderElement element, Date date);
}
