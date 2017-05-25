package pl.sodexo.it.gryf.model.publicbenefits.orders;

import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by tbilski on 2017-05-21.
 */
@Entity
@Table(name = "ORDER_FLOW_ST_TRANS_ELEM_FLAGS", schema = "APP_PBE")
public class OrderFlowStatusTransitionElementFlag extends GryfEntity {

    //STATIC FIELDS - FLAGS

    public static final String FLAG_INSERTABLE = "I";
    public static final String FLAG_UPDATEABLE = "U";
    public static final String FLAG_MANDATORY = "M";

    //FIELDS

    @EmbeddedId
    protected OrderFlowStatusTransitionElementFlagPK id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ORDER_FLOW_ID", referencedColumnName = "ORDER_FLOW_ID", insertable = false, updatable = false),
            @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID", insertable = false, updatable = false),
            @JoinColumn(name = "ACTION_ID", referencedColumnName = "ACTION_ID", insertable = false, updatable = false)
    })
    private OrderFlowStatusTransition orderFlowStatusTransition;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID", insertable = false, updatable = false),
            @JoinColumn(name = "ELEMENT_ID", referencedColumnName = "ELEMENT_ID", insertable = false, updatable = false)
    })
    private OrderFlowElementInStatus orderFlowElementInStatus;

    @Column(name = "FLAGS")
    private String flags;

    //GETTERS & SETTERS

    public OrderFlowStatusTransitionElementFlagPK getId() {
        return id;
    }

    public void setId(OrderFlowStatusTransitionElementFlagPK id) {
        this.id = id;
    }

    public OrderFlowStatusTransition getOrderFlowStatusTransition() {
        return orderFlowStatusTransition;
    }

    public void setOrderFlowStatusTransition(OrderFlowStatusTransition orderFlowStatusTransition) {
        this.orderFlowStatusTransition = orderFlowStatusTransition;
    }

    public OrderFlowElementInStatus getOrderFlowElementInStatus() {
        return orderFlowElementInStatus;
    }

    public void setOrderFlowElementInStatus(OrderFlowElementInStatus orderFlowElementInStatus) {
        this.orderFlowElementInStatus = orderFlowElementInStatus;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    //PUBLIC METHODS

    public boolean isInsertable(){
        return !GryfStringUtils.isEmpty(flags) && flags.contains(FLAG_INSERTABLE);
    }

    public boolean isUpdatable(){
        return !GryfStringUtils.isEmpty(flags) && flags.contains(FLAG_UPDATEABLE);
    }

    public boolean isMandatory(){
        return !GryfStringUtils.isEmpty(flags) && flags.contains(FLAG_MANDATORY);
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
        return Objects.equals(id, ((OrderFlowStatusTransitionElementFlag) o).id);
    }
}
