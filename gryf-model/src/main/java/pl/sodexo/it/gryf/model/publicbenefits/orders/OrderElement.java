/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.orders;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.AuditableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Instances of attributes for orders
 * @author Michal.CHWEDCZUK.ext
 */
@Entity
@Table(name = "ORDER_ELEMENTS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = OrderElement.FIND_DTO_FACTORY_BY_ORDER_TO_MODIFY, query =
                "SELECT distinct new pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder (o, e, fe, eis) " +
                "FROM OrderEntity o " +
                "JOIN o.orderElements e " +
                "JOIN e.orderFlowElement fe " +
                "JOIN fe.orderFlowElementInStatuses eis " +
                "WHERE o.status = eis.status " +
                "AND o.id = :id " +
                "ORDER by eis.pos"),
        @NamedQuery(name = OrderElement.FIND_DTO_FACTORY_BY_ORDER_TO_PREVIEW, query =
                "SELECT distinct new pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder (o, e, fe) " +
                "FROM OrderEntity o " +
                "JOIN o.orderElements e " +
                "JOIN e.orderFlowElement fe " +
                "JOIN fe.orderFlowElementInStatuses eis " +
                "WHERE o.id = :id " +
                "AND ((eis.status IN " +
                        "(SELECT ofstx.nextStatus " +
                        " FROM OrderEntity ox " +
                        " JOIN ox.orderFlow ofx " +
                        " JOIN ofx.orderFlowStatusTransitions ofstx " +
                        " WHERE ox.id = :id)) " +
                     "OR " +
                     "(eis.status IN " +
                        "(SELECT ofx.initialStatus " +
                        " FROM OrderEntity ox " +
                        " JOIN ox.orderFlow ofx " +
                        " WHERE ox.id = :id))) " +
                "ORDER by e.id"),
        @NamedQuery(name = OrderElement.FIND_BY_ORDER_AND_ELEMENTS, query =
                "SELECT oe " +
                "FROM OrderElement oe " +
                "JOIN oe.orderFlowElement efe " +
                "WHERE oe.order.id = :id " +
                "AND efe.elementId in :elements ")
})
public class OrderElement extends AuditableEntity {

    //STATIC FIELDS - ATRIBUTES
    public static final String REQUIRED_DATE_ATTR_NAME = "requiredDate";
    public static final String COMPLETED_DATE_ATTR_NAME = "completedDate";

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_DTO_FACTORY_BY_ORDER_TO_MODIFY = "OrderElement.findDtoFactoryByOrderToModify";
    public static final String FIND_DTO_FACTORY_BY_ORDER_TO_PREVIEW = "OrderElement.findDtoFactoryByOrderToPreview";
    public static final String FIND_BY_ORDER_AND_ELEMENTS = "OrderElement.findByOrderAndElements";
    //FIELDS

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @Size(max = 500)
    @Column(name = "VALUE_VARCHAR")
    private String valueVarchar;

    @Column(name = "VALUE_NUMBER")
    private BigDecimal valueNumber;

    @Column(name = "VALUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date valueDate;

    @Column(name = "REQUIRED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requiredDate;

    @Column(name = "COMPLETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedDate;

    @JsonBackReference("orderElements")
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Order order;

    @JsonBackReference("orderElements")
    @JoinColumn(name = "ELEMENT_ID", referencedColumnName = "ELEMENT_ID")
    @ManyToOne(optional = false)
    private OrderFlowElement orderFlowElement;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "orderElement")
    private OrderElementClob clob;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValueVarchar() {
        return valueVarchar;
    }

    public void setValueVarchar(String valueVarchar) {
        this.valueVarchar = valueVarchar;
    }

    public BigDecimal getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(BigDecimal valueNumber) {
        this.valueNumber = valueNumber;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderFlowElement getOrderFlowElement() {
        return orderFlowElement;
    }

    public void setOrderFlowElement(OrderFlowElement elementId) {
        this.orderFlowElement = elementId;
    }

    public OrderElementClob getClob() {
        return clob;
    }

    public void setClob(OrderElementClob clob) {
        this.clob = clob;
    }

    //EQUALS & HASH CODE

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
        return Objects.equals(id, ((OrderElement) o).id);
    }

    @Override
    public String toString() {
        return "pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement[ id=" + id + " ]";
    }

}
