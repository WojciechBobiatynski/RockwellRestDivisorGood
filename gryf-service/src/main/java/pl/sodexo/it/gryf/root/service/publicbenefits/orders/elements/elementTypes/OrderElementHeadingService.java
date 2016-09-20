package pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.OrderElementBaseService;

import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementHeadingDTO;

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
