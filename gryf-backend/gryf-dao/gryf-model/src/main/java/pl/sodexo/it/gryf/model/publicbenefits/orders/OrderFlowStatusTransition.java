package pl.sodexo.it.gryf.model.publicbenefits.orders;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.BooleanConverter;
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
@ToString(exclude = {"orderFlow", "nextStatus", "status", "orderFlowStatusTransSqlList"})
@Entity
@Table(name = "ORDER_FLOW_STATUS_TRANSITIONS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = OrderFlowStatusTransition.FIND_DTO_BY_ORDER, query = "SELECT new pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowTransitionDTOBuilder (ofst, ofs) " +
                                                                                "FROM OrderEntity o " +
                                                                                "JOIN o.status ofs " +
                                                                                "JOIN ofs.orderFlowStatusTransitions ofst " +
                                                                                "WHERE o.id = :id " +
                                                                                "AND o.orderFlow.id = ofst.orderFlow.id " +
                                                                                "ORDER BY ofst.id.actionId"),
        @NamedQuery(name = "OrderFlowStatusTransition.countAutomaticTransitionByOrder", query = "select count(o) from OrderEntity o join o.status s join s.orderFlowStatusTransitions t " +
                                                                    "where o.id = :orderId and t.automatic = true ")})
public class OrderFlowStatusTransition extends GryfEntity {

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_DTO_BY_ORDER = "OrderFlowStatusTransition.findDtoByOrder";

    //STATIC FIELDS - COMPONENT NAME FIELDS

    private static final String ACTION_SERVICE_PREFIX = "";
    private static final String ACTION_SERVICE_SUFFIX = "ActionService";

    //FIELDS

    @EmbeddedId
    protected OrderFlowStatusTransitionPK id;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ACTION_NAME")
    private String actionName;

    @Size(max = 500)
    @Column(name = "ACTION_DESCRIPTION")
    private String actionDescription;

    @Size(max = 500)
    @Column(name = "ACTION_CLASS")
    private String actionClass;

    @Size(max = 50)
    @Column(name = "AUG_ID_REQUIRED")
    private String augIdRequired;

    @JsonBackReference("orderFlowStatusTransitions")
    @JoinColumn(name = "ORDER_FLOW_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OrderFlow orderFlow;

    @JoinColumn(name = "NEXT_STATUS_ID", referencedColumnName = "STATUS_ID")
    @ManyToOne(optional = false)
    private OrderFlowStatus nextStatus;

    @JsonBackReference("orderFlowStatusTransitions")
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OrderFlowStatus status;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "AUTOMATIC")
    private boolean automatic;

    @JsonManagedReference("orderFlowStatusTransSqlList")
    @OrderBy(value = "id ASC")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderFlowStatusTransition")
    private List<OrderFlowStatusTransSql> orderFlowStatusTransSqlList;

    @JsonManagedReference("orderFlowStatusTransitionElementFlags")
    @OneToMany(mappedBy = "orderFlowStatusTransition")
    private List<OrderFlowStatusTransitionElementFlag> orderFlowStatusTransitionElementFlags;

    //GETTERS & SETTERS

    public OrderFlowStatusTransitionPK getId() {
        return id;
    }

    public void setId(OrderFlowStatusTransitionPK id) {
        this.id = id;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getActionClass() {
        return actionClass;
    }

    public void setActionClass(String actionClass) {
        this.actionClass = actionClass;
    }

    public String getAugIdRequired() {
        return augIdRequired;
    }

    public void setAugIdRequired(String augIdRequired) {
        this.augIdRequired = augIdRequired;
    }

    public OrderFlow getOrderFlow() {
        return orderFlow;
    }

    public void setOrderFlow(OrderFlow orderFlow) {
        this.orderFlow = orderFlow;
    }

    public OrderFlowStatus getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(OrderFlowStatus nextStatus) {
        this.nextStatus = nextStatus;
    }

    public OrderFlowStatus getStatus() {
        return status;
    }

    public void setStatus(OrderFlowStatus orderFlowStatus) {
        this.status = orderFlowStatus;
    }

    public Boolean getAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    //LIST METHODS

    private List<OrderFlowStatusTransSql> getInitializedOrderFlowStatusTransSqlList() {
        if (orderFlowStatusTransSqlList == null)
            orderFlowStatusTransSqlList = new ArrayList<>();
        return orderFlowStatusTransSqlList;
    }

    public List<OrderFlowStatusTransSql> getOrderFlowStatusTransSqlList() {
        return Collections.unmodifiableList(getInitializedOrderFlowStatusTransSqlList());
    }

    private List<OrderFlowStatusTransitionElementFlag> getInitializedOrderFlowStatusTransitionElementFlags() {
        if (orderFlowStatusTransitionElementFlags == null)
            orderFlowStatusTransitionElementFlags = new ArrayList<>();
        return orderFlowStatusTransitionElementFlags;
    }

    public List<OrderFlowStatusTransitionElementFlag> getOrderFlowStatusTransitionElementFlags() {
        return Collections.unmodifiableList(getInitializedOrderFlowStatusTransitionElementFlags());
    }

    //PUBLIC METHODS - COMPONENT METHODS

    public String getActionBeanName(){
        return getActionBeanName(actionClass);
    }

    public static String getActionBeanName(String value){
        return (value != null) ? (ACTION_SERVICE_PREFIX + value + ACTION_SERVICE_SUFFIX) : null;
    }

    //HASH CODE & EQUALS

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((OrderFlowStatusTransition) o).id);
    }
}
