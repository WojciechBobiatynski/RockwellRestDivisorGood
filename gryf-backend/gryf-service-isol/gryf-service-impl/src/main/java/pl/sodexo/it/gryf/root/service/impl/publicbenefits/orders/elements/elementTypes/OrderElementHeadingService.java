package pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementHeadingDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.OrderElementBaseService;

/**
 * Serwis dla nagłówków (headings)
 * @author Marcel.GOLUNSKI
 */
@Service
public class OrderElementHeadingService extends OrderElementBaseService<OrderElementHeadingDTO> {

    @Override
    public OrderElementHeadingDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementHeadingDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementHeadingDTO dto) {
    }
    //PROTECTED METHODS - OVERRIDE VALIDATION

}
