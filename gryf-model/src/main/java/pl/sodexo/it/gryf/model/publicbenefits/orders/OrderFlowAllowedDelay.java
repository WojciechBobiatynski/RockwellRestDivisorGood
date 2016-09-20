/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.orders;

import pl.sodexo.it.gryf.model.DayType;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Entity
@Table(name = "ORDER_FLOW_ALLOWED_DELAYS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = OrderFlowAllowedDelay.FIND_BY_ORDER_FLOW_AND_DELAY_TYPE, query =
                "SELECT o " +
                "FROM OrderFlowAllowedDelay o " +
                "WHERE o.orderFlow.id = :orderFlowId " +
                "AND o.delayType.id =  :orderFlowAllowedDelayTypeId"),

})
public class OrderFlowAllowedDelay extends GryfEntity {
    
    public static final String FIND_BY_ORDER_FLOW_AND_DELAY_TYPE = "OrderFlowAllowedDelay.findByOrderFlowAndDelayType";

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Column(name = "ID")
    private Long id;
    @NotNull
    @Column(name = "DELAY_VALUE")
    private Integer delayValue;
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "DELAY_DAYS_TYPE")
    @Enumerated(EnumType.STRING)
    private DayType delayDaysType;
    @Size(max = 200)
    @Column(name = "REMARKS")
    private String remarks;
    @JoinColumn(name = "ORDER_FLOW_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrderFlow orderFlow;
    @JoinColumn(name = "DELAY_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrderFlowAllowedDelayType delayType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDelayValue() {
        return delayValue;
    }

    public void setDelayValue(Integer delayValue) {
        this.delayValue = delayValue;
    }

    public DayType getDelayDaysType() {
        return delayDaysType;
    }

    public void setDelayDaysType(DayType delayDaysType) {
        this.delayDaysType = delayDaysType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public OrderFlow getOrderFlow() {
        return orderFlow;
    }

    public void setOrderFlow(OrderFlow orderFlow) {
        this.orderFlow = orderFlow;
    }

    public OrderFlowAllowedDelayType getDelayType() {
        return delayType;
    }

    public void setDelayType(OrderFlowAllowedDelayType delayType) {
        this.delayType = delayType;
    }

    
    

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
        return Objects.equals(id, ((OrderFlowAllowedDelay) o).id);
    }
    

    @Override
    public String toString() {
        return "pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowAllowedDelay[ id=" + id + " ]";
    }
    
}
