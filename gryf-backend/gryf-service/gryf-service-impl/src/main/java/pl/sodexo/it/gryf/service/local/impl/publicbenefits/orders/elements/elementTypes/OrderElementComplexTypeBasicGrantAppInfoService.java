package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeBasicGrantAppInfoDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.orders.action.OrderElementDTOProvider;

/**
 * Created by tomasz.bilski.ext on 2015-08-27.
 */
@Service
public class OrderElementComplexTypeBasicGrantAppInfoService extends OrderElementBaseService<OrderElementComplexTypeBasicGrantAppInfoDTO> {

    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeBasicGrantAppInfoDTO createElement(OrderElementDTOBuilder builder) {
        return OrderElementDTOProvider.createOrderElementComplexTypeBasicGrantAppInfoDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeBasicGrantAppInfoDTO dto) {

    }
}
