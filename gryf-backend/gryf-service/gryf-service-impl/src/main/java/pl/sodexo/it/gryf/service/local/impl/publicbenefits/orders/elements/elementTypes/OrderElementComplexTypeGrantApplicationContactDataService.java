package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeGrantApplicationContactDataDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.orders.action.OrderElementDTOProvider;

/**
 * Created by tomasz.bilski.ext on 2015-09-17.
 */
@Service
public class OrderElementComplexTypeGrantApplicationContactDataService extends OrderElementBaseService<OrderElementComplexTypeGrantApplicationContactDataDTO> {

    @Override
    public OrderElementComplexTypeGrantApplicationContactDataDTO createElement(OrderElementDTOBuilder builder) {
        return OrderElementDTOProvider.createOrderElementComplexTypeGrantApplicationContactDataDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeGrantApplicationContactDataDTO dto) {

    }
}
