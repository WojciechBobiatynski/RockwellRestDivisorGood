/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@ToString(exclude = {"orderFlow", "status"})
@Entity
@Table(name = "ORDER_FLOW_STATUS_PROPERTIES", schema = "APP_PBE")
public class OrderFlowStatusProperty extends GryfEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Column(name = "ID")
    private Long id;
    @NotNull
    @JoinColumn(name = "ORDER_FLOW_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrderFlow orderFlow;
    @NotNull
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID")
    @ManyToOne(optional = false)
    private OrderFlowStatus status;
    @Size(max = 1)
    @Column(name = "FINAL_STATUS")
    private String finalStatus;
    @Size(max = 1)
    @Column(name = "SUCCESS_STATUS")
    private String successStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderFlow getOrderFlow() {
        return orderFlow;
    }

    public void setOrderFlow(OrderFlow orderFlow) {
        this.orderFlow = orderFlow;
    }

    public OrderFlowStatus getStatus() {
        return status;
    }

    public void setStatus(OrderFlowStatus status) {
        this.status = status;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public String getSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(String successStatus) {
        this.successStatus = successStatus;
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
        return Objects.equals(id, ((OrderFlowStatusProperty) o).id);
    }
}
