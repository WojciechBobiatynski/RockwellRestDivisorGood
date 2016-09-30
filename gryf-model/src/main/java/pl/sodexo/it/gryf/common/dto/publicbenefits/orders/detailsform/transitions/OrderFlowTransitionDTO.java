package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.transitions;

import lombok.ToString;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionPK;

/**
 * DTO for Action
 *
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
@ToString
public class OrderFlowTransitionDTO {
    private String actionName;
    private OrderFlowStatusTransitionPK id;
    private String privilege;

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
