package pl.sodexo.it.gryf.model.publicbenefits.orders;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@ToString(exclude = {"orderFlows", "orders", "orderFlowStatusTransitions", "orderFlowElementsInStatus"})
@Entity
@Table(name = "ORDER_FLOW_STATUSES", schema = "APP_PBE")
@NamedQueries({@NamedQuery(name = "OrderFlowStatus.findByGrantProgram", query = "select distinct t.nextStatus from OrderFlowForGrantProgram o " +
        "join o.orderFlow oflow join oflow.orderFlowStatusTransitions t " +
        "where o.grantProgram.id = :grantProgramId " +
        "union " +
        "select distinct t.status from OrderFlowForGrantProgram o join o.orderFlow oflow join oflow.orderFlowStatusTransitions t " +
        "where o.grantProgram.id = :grantProgramId ")})
public class OrderFlowStatus extends GryfEntity implements DictionaryEntity {

    //STATIC FIELDS - ATRIBUTES

    public static final String STATUS_ID_ATTR_NAME = "statusId";

    //FIELDS

    @Id
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "STATUS_ID")
    private String statusId;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "STATUS_NAME")
    private String statusName;

    @JsonBackReference("initialStatus")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "initialStatus")
    private List<OrderFlow> orderFlows;

    @JsonBackReference("status")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private List<Order> orders;

    @JsonManagedReference("orderFlowStatusTransitions")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private List<OrderFlowStatusTransition> orderFlowStatusTransitions;

    @OrderBy("pos")
    @JsonManagedReference("orderFlowElementsInStatus")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private List<OrderFlowElementInStatus> orderFlowElementsInStatus;

    //GETTERS & SETTERS

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    //LIST METHODS

    private List<OrderFlow> getInitializedOrderFlows() {
        if (orderFlows == null)
            orderFlows = new ArrayList<>();
        return orderFlows;
    }

    public List<OrderFlow> getOrderFlows() {
        return Collections.unmodifiableList(getInitializedOrderFlows());
    }

    private List<Order> getInitializedOrders() {
        if (orders == null)
            orders = new ArrayList<>();
        return orders;
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(getInitializedOrders());
    }

    private List<OrderFlowStatusTransition> getInitializedOrderFlowStatusTransitions() {
        if (orderFlowStatusTransitions == null)
            orderFlowStatusTransitions = new ArrayList<>();
        return orderFlowStatusTransitions;
    }

    public List<OrderFlowStatusTransition> getOrderFlowStatusTransitions() {
        return Collections.unmodifiableList(getInitializedOrderFlowStatusTransitions());
    }

    private List<OrderFlowElementInStatus> getInitializedOrderFlowElementsInStatus() {
        if (orderFlowElementsInStatus == null)
            orderFlowElementsInStatus = new ArrayList<>();
        return orderFlowElementsInStatus;
    }

    public List<OrderFlowElementInStatus> getOrderFlowElementsInStatus() {
        return Collections.unmodifiableList(getInitializedOrderFlowElementsInStatus());
    }

    //DICTIONARY METHODS

    @Override
    public Object getDictionaryId() {
        return statusId;
    }

    @Override
    public String getDictionaryName() {
        return String.format("%s (%s)", statusName, statusId);
    }

    @Override
    public String getOrderField() {
        return "statusName";
    }

    @Override
    public String getActiveField() {
        return null;
    }

    //HASH CODE & EQUALS

    @Override
    public int hashCode() {
        return Objects.hashCode(statusId) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(statusId, ((OrderFlowStatus) o).statusId);
    }
}
