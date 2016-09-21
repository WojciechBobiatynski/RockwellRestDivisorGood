/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.orders;

import com.fasterxml.jackson.annotation.JsonBackReference;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@Entity
@Table(name = "ORDER_FLOW_ELEMENTS_IN_STATUS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = OrderFlowElementInStatus.FIND_BY_STATUS_ID_AND_ELEMENT_ID, query =
                "SELECT o " +
                "FROM OrderFlowElementInStatus o " +
                "WHERE o.status.statusId = :orderStatusId " +
                "AND o.orderFlowElement.elementId = :elementId"),

})
public class OrderFlowElementInStatus extends GryfEntity {

    public static final String FIND_BY_STATUS_ID_AND_ELEMENT_ID = "OrderFlowElementInStatus.findByOrderStatusIdAndElementId";
    //STATIC FIELDS - FLAGS

    public static final String FLAG_INSERTABLE = "I";
    public static final String FLAG_UPDATEABLE = "U";
    public static final String FLAG_MANDATORY = "M";

    //FIELDS

    @EmbeddedId
    protected OrderFlowElementInStatusPK id;

    @NotNull
    @Column(name = "POS")
    private Integer pos;

    @JoinColumn(name = "ALLOWED_DELAY_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrderFlowAllowedDelayType orderFlowAllowedDelayType;
    
    @Size(max = 10)
    @Column(name = "FLAGS")
    private String flags;

    @Size(max = 50)
    @Column(name = "AUG_ID_REQUIRED")
    private String augIdRequired;

    @JsonBackReference("orderFlowElementsInStatus")
    @JoinColumn(name = "ELEMENT_ID", referencedColumnName = "ELEMENT_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OrderFlowElement orderFlowElement;

    @JsonBackReference("orderFlowElementsInStatus")
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OrderFlowStatus status;

    //GETTERS & SETTERS

    public OrderFlowElementInStatusPK getId() {
        return id;
    }

    public void setId(OrderFlowElementInStatusPK id) {
        this.id = id;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getAugIdRequired() {
        return augIdRequired;
    }

    public void setAugIdRequired(String augIdRequired) {
        this.augIdRequired = augIdRequired;
    }

    public OrderFlowElement getOrderFlowElement() {
        return orderFlowElement;
    }

    public void setOrderFlowElement(OrderFlowElement orderFlowElement) {
        this.orderFlowElement = orderFlowElement;
    }

    public OrderFlowStatus getStatus() {
        return status;
    }

    public void setStatus(OrderFlowStatus orderFlowStatus) {
        this.status = orderFlowStatus;
    }

    public OrderFlowAllowedDelayType getOrderFlowAllowedDelayType() {
        return orderFlowAllowedDelayType;
    }

    public void setOrderFlowAllowedDelayType(OrderFlowAllowedDelayType orderFlowAllowedDelayType) {
        this.orderFlowAllowedDelayType = orderFlowAllowedDelayType;
    }



    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    //PUBLIC METHODS

    public boolean isInsertable(){
        return !StringUtils.isEmpty(flags) && flags.contains(FLAG_INSERTABLE);
    }

    public boolean isUpdatable(){
        return !StringUtils.isEmpty(flags) && flags.contains(FLAG_UPDATEABLE);
    }

    public boolean isMandatory(){
        return !StringUtils.isEmpty(flags) && flags.contains(FLAG_MANDATORY);
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
        return Objects.equals(id, ((OrderFlowElementInStatus) o).id);
    }

    @Override
    public String toString() {
        return "pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus[ id=" + id + " ]";
    }
    
}
