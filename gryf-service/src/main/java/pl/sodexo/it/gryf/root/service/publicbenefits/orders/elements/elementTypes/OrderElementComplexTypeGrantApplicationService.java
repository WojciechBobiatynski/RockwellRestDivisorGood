package pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeGrantApplicationDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.OrderElementBaseService;

/**
 * @author Marcel.GOLUNSKI
 */
@Service
@Transactional
public class OrderElementComplexTypeGrantApplicationService extends OrderElementBaseService<OrderElementComplexTypeGrantApplicationDTO> {

    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeGrantApplicationDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementComplexTypeGrantApplicationDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeGrantApplicationDTO dto) {
    }
}
