package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by tbilski on 2017-05-21.
 */
@ToString
@Embeddable
public class OrderFlowStatusTransitionElementFlagPK implements Serializable {


    @NotNull
    @Column(name = "ORDER_FLOW_ID")
    private Long orderFlowId;

    @NotNull
    @Column(name = "STATUS_ID")
    private String statusId;

    @NotNull
    @Column(name = "ACTION_ID")
    private Long actionId;


    @NotNull
    @Column(name = "ELEMENT_ID")
    private String elementId;

    public OrderFlowStatusTransitionElementFlagPK() {
    }

    public OrderFlowStatusTransitionElementFlagPK(Long orderFlowId, String statusId, Long actionId, String elementId) {
        this.orderFlowId = orderFlowId;
        this.statusId = statusId;
        this.actionId = actionId;
        this.elementId = elementId;
    }

    //GETTERS & SETETRS

    public Long getOrderFlowId() {
        return orderFlowId;
    }

    public void setOrderFlowId(Long orderFlowId) {
        this.orderFlowId = orderFlowId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }


    //PUBLIC METHODS - COMPONENT METHODS

    @Override
    public int hashCode() {
        return (Objects.hashCode(orderFlowId) % 102 +
                Objects.hashCode(statusId) % 102 +
                Objects.hashCode(actionId) % 102 +
                Objects.hashCode(elementId) % 102);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(orderFlowId, ((OrderFlowStatusTransitionElementFlagPK) o).orderFlowId) &&
                Objects.equals(statusId, ((OrderFlowStatusTransitionElementFlagPK) o).statusId) &&
                Objects.equals(actionId, ((OrderFlowStatusTransitionElementFlagPK) o).actionId) &&
                Objects.equals(actionId, ((OrderFlowStatusTransitionElementFlagPK) o).actionId);
    }
}