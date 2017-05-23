package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-18.
 */
@ToString
public class OrderElementDTOBuilder {

    //FIELDS

    private Order order;

    private OrderElement element;

    private OrderFlowElement orderFlowElement;

    private OrderFlowElementInStatus orderFlowElementInStatus;

    private List<OrderFlowStatusTransitionElementFlag> orderFlowStatusTransitionElementFlags;

    //CONSTRUCTORS

    public OrderElementDTOBuilder() {
        this(null, null, null, null);
    }

    public OrderElementDTOBuilder(Order order,
                                  OrderElement element,
                                  OrderFlowElement orderFlowElement) {
        this(order, element, orderFlowElement, null);
    }


    public OrderElementDTOBuilder(Order order,
                                  OrderElement element,
                                  OrderFlowElement orderFlowElement,
                                  OrderFlowElementInStatus orderFlowElementInStatus) {
        this.order = order;
        this.element = element;
        this.orderFlowElement = orderFlowElement;
        this.orderFlowElementInStatus = orderFlowElementInStatus;
        this.orderFlowStatusTransitionElementFlags = orderFlowElementInStatus != null ?
                orderFlowElementInStatus.getOrderFlowStatusTransitionElementFlags() : new ArrayList<>();
    }

    //GETTERS & SETTERS

    public OrderElement getElement() {
        return element;
    }

    public Order getOrder() {
        return order;
    }

    public OrderFlowElement getOrderFlowElement() {
        return orderFlowElement;
    }

    public OrderFlowElementInStatus getOrderFlowElementInStatus() {
        return orderFlowElementInStatus;
    }

    public List<OrderFlowStatusTransitionElementFlag> getOrderFlowStatusTransitionElementFlags() {
        return orderFlowStatusTransitionElementFlags;
    }
}
