package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;

/**
 * Created by tomasz.bilski.ext on 2015-08-18.
 */
@ToString
public class OrderFlowTransitionDTOBuilder {

    private OrderFlowStatusTransition orderFlowStatusTransition;

    private OrderFlowStatus orderFlowStatus;

    public OrderFlowTransitionDTOBuilder(OrderFlowStatusTransition orderFlowStatusTransition,
            OrderFlowStatus orderFlowStatus) {
        this.orderFlowStatus = orderFlowStatus;
        this.orderFlowStatusTransition = orderFlowStatusTransition;
    }

    public OrderFlowStatusTransition getOrderFlowStatusTransition() {
        return orderFlowStatusTransition;
    }

    public void setOrderFlowStatusTransition(OrderFlowStatusTransition orderFlowStatusTransition) {
        this.orderFlowStatusTransition = orderFlowStatusTransition;
    }

    public OrderFlowStatus getOrderFlowStatus() {
        return orderFlowStatus;
    }

    public void setOrderFlowStatus(OrderFlowStatus orderFlowStatus) {
        this.orderFlowStatus = orderFlowStatus;
    }
}
