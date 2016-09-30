package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeEnterprisePaymentInfoDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.orders.action.OrderElementDTOProvider;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Service
public class OrderElementComplexTypeEnterprisePaymentInfoService extends OrderElementBaseService<OrderElementComplexTypeEnterprisePaymentInfoDTO> {

    @Autowired
    private OrderServiceLocal orderServiceLocal;
    
    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeEnterprisePaymentInfoDTO createElement(OrderElementDTOBuilder builder) {
        return OrderElementDTOProvider.createOrderElementComplexTypeEnterprisePaymentInfoDTO(builder, orderServiceLocal.getPaymentAmount(builder.getOrder()));
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeEnterprisePaymentInfoDTO dto) {
    }
}
