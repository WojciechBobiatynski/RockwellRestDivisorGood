package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeBasicContractInfoDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.orders.action.OrderElementDTOProvider;

/**
 * Created by Isolution on 2016-11-21.
 */
@Service
public class OrderElementComplexTypeBasicContractInfoService extends OrderElementBaseService<OrderElementComplexTypeBasicContractInfoDTO> {

    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeBasicContractInfoDTO createElement(OrderElementDTOBuilder builder) {
        return null;
        //return OrderElementDTOProvider.createOrderElementComplexTypeBasicContractInfoDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeBasicContractInfoDTO dto) {

    }
}
