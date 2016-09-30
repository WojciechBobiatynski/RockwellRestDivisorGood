package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.orders;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.transitions.OrderFlowTransitionDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowTransitionDTOBuilder;

/**
 * Maper mapujący encję Enterprise na EnterpriseDto
 *
 * Created by jbentyn on 2016-09-26.
 */
public final class OrderFlowTransitionDTOProvider {

    private OrderFlowTransitionDTOProvider(){};

    //TODO AdamK: na razie tak
    public static OrderFlowTransitionDTO createOrderFlowTransitionDTO(OrderFlowTransitionDTOBuilder builder) {
        OrderFlowTransitionDTO dto = new OrderFlowTransitionDTO();
        dto.setActionName(builder.getOrderFlowStatusTransition().getActionName());
        dto.setId(builder.getOrderFlowStatusTransition().getId());
        dto.setPrivilege(builder.getOrderFlowStatusTransition().getAugIdRequired());
        return dto;
    }
}
