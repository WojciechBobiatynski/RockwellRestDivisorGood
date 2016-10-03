package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.orders;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.transitions.OrderFlowStatusTransitionPKDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.transitions.OrderFlowTransitionDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionPK;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowTransitionDTOBuilder;

/**
 * Klasa dostarczająca obiekty DTO dla encji związanych z OrderFlow
 *
 * Created by jbentyn on 2016-09-26.
 */
public final class OrderFlowTransitionDTOProvider {

    private OrderFlowTransitionDTOProvider(){};

    //TODO AdamK: na razie tak
    public static OrderFlowTransitionDTO createOrderFlowTransitionDTO(OrderFlowTransitionDTOBuilder builder) {
        OrderFlowTransitionDTO dto = new OrderFlowTransitionDTO();
        dto.setActionName(builder.getOrderFlowStatusTransition().getActionName());
        dto.setId(createOrderFlowStatusTransitionPKDTO(builder.getOrderFlowStatusTransition().getId()));
        dto.setPrivilege(builder.getOrderFlowStatusTransition().getAugIdRequired());
        return dto;
    }

    private static OrderFlowStatusTransitionPKDTO createOrderFlowStatusTransitionPKDTO(OrderFlowStatusTransitionPK entity){
        OrderFlowStatusTransitionPKDTO dto = new OrderFlowStatusTransitionPKDTO();
        dto.setActionId(entity.getActionId());
        dto.setOrderFlowId(entity.getOrderFlowId());
        dto.setStatusId(entity.getStatusId());
        return dto;
    }
}
