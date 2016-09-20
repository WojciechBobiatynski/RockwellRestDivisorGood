package pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeBasicGrantAppInfoDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.OrderElementBaseService;

/**
 * Created by tomasz.bilski.ext on 2015-08-27.
 */
@Service
public class OrderElementComplexTypeBasicGrantAppInfoService extends OrderElementBaseService<OrderElementComplexTypeBasicGrantAppInfoDTO> {

    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeBasicGrantAppInfoDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementComplexTypeBasicGrantAppInfoDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeBasicGrantAppInfoDTO dto) {

    }
}
