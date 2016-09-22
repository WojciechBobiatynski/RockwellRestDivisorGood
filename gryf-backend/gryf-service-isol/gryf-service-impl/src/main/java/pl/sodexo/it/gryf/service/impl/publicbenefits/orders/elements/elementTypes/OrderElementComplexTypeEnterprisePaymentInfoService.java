package pl.sodexo.it.gryf.service.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeEnterprisePaymentInfoDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Service
public class OrderElementComplexTypeEnterprisePaymentInfoService extends OrderElementBaseService<OrderElementComplexTypeEnterprisePaymentInfoDTO> {

    @Autowired
    private OrderService orderService;
    
    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeEnterprisePaymentInfoDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementComplexTypeEnterprisePaymentInfoDTO(builder, orderService.getPaymentAmount(builder.getOrder()));
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeEnterprisePaymentInfoDTO dto) {
    }
}
