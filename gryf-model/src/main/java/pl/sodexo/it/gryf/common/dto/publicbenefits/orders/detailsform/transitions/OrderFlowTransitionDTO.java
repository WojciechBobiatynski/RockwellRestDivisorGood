package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.transitions;

import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatus;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransition;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionPK;

/**
 * DTO for Action
 *
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
public class OrderFlowTransitionDTO {
    private String actionName;
    private OrderFlowStatusTransitionPK id;
    private String privilege;

    public OrderFlowTransitionDTO(OrderFlowStatusTransition orderFlowStatusTransition,
                                  OrderFlowStatus orderFlowStatus) {
        this.actionName = orderFlowStatusTransition.getActionName();
        this.id = orderFlowStatusTransition.getId();
        this.privilege = orderFlowStatusTransition.getAugIdRequired();
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public OrderFlowStatusTransitionPK getId() {
        return id;
    }

    public void setId(OrderFlowStatusTransitionPK id) {
        this.id = id;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
}
