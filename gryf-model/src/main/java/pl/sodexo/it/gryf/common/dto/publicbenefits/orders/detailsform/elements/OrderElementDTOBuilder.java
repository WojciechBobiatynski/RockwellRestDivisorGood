package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;

/**
 * Created by tomasz.bilski.ext on 2015-08-18.
 */
public class OrderElementDTOBuilder {

    //FIELDS

    private Order order;

    private OrderElement element;

    private OrderFlowElement orderFlowElement;

    private OrderFlowElementInStatus orderFlowElementInStatus;

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
}
