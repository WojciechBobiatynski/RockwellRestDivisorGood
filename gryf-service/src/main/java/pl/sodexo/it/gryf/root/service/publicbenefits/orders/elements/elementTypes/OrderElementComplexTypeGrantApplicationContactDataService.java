package pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeGrantApplicationContactDataDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.OrderElementBaseService;

/**
 * Created by tomasz.bilski.ext on 2015-09-17.
 */
@Service
public class OrderElementComplexTypeGrantApplicationContactDataService extends OrderElementBaseService<OrderElementComplexTypeGrantApplicationContactDataDTO> {

    @Override
    public OrderElementComplexTypeGrantApplicationContactDataDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementComplexTypeGrantApplicationContactDataDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeGrantApplicationContactDataDTO dto) {

    }
}
