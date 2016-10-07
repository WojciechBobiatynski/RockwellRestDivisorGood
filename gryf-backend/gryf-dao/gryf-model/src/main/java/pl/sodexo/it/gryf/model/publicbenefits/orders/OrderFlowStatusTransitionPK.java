package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Michal.CHWEDCZUK.ext
 */
@ToString
@Embeddable
public class OrderFlowStatusTransitionPK implements Serializable {

    @NotNull
    @Column(name = "ORDER_FLOW_ID")
    private Long orderFlowId;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "STATUS_ID")
    private String statusId;

    @NotNull
    @Column(name = "ACTION_ID")
    private Long actionId;

    //CONSTRCUTORS

    public OrderFlowStatusTransitionPK() {
    }

    public OrderFlowStatusTransitionPK(Long orderFlowId, String statusId, Long actionId) {
        this.orderFlowId = orderFlowId;
        this.statusId = statusId;
        this.actionId = actionId;
    }

    //GETTERS & SETTERS

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

    //PUBLIC METHODS - COMPONENT METHODS

    @Override
    public int hashCode() {
        return (Objects.hashCode(orderFlowId) % 102 +
                Objects.hashCode(statusId) % 102 +
                Objects.hashCode(actionId) % 102);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(orderFlowId, ((OrderFlowStatusTransitionPK) o).orderFlowId) &&
                Objects.equals(statusId, ((OrderFlowStatusTransitionPK) o).statusId) &&
                Objects.equals(actionId, ((OrderFlowStatusTransitionPK) o).actionId);
    }
}
