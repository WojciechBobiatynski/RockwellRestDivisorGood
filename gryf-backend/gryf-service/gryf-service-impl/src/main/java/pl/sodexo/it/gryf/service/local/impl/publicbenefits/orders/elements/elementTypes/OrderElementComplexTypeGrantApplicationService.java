package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeGrantApplicationDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.orders.action.OrderElementDTOProvider;

/**
 * @author Marcel.GOLUNSKI
 */
@Service
@Transactional
public class OrderElementComplexTypeGrantApplicationService extends OrderElementBaseService<OrderElementComplexTypeGrantApplicationDTO> {

    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeGrantApplicationDTO createElement(OrderElementDTOBuilder builder) {
        return OrderElementDTOProvider.createOrderElementComplexTypeGrantApplicationDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeGrantApplicationDTO dto) {
    }
}
