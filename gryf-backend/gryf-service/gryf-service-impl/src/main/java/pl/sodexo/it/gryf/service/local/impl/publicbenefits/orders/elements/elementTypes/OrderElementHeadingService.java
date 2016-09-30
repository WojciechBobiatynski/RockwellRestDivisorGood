package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementHeadingDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.orders.action.OrderElementDTOProvider;

/**
 * Serwis dla nagłówków (headings)
 * @author Marcel.GOLUNSKI
 */
@Service
public class OrderElementHeadingService extends OrderElementBaseService<OrderElementHeadingDTO> {

    @Override
    public OrderElementHeadingDTO createElement(OrderElementDTOBuilder builder) {
        return OrderElementDTOProvider.createOrderElementHeadingDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementHeadingDTO dto) {
    }
    //PROTECTED METHODS - OVERRIDE VALIDATION

}
