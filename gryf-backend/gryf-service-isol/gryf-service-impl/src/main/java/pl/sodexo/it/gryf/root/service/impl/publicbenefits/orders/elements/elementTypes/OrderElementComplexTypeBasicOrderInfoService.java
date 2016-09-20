package pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeBasicOrderInfoDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.OrderElementBaseService;

/**
 * Created by tomasz.bilski.ext on 2015-08-27.
 */
@Service
public class OrderElementComplexTypeBasicOrderInfoService extends OrderElementBaseService<OrderElementComplexTypeBasicOrderInfoDTO> {

    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeBasicOrderInfoDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementComplexTypeBasicOrderInfoDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeBasicOrderInfoDTO dto) {
    }
}
