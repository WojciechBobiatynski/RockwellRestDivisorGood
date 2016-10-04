/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.orders;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@ToString(exclude = "orderFlowStatusTransition")
@Entity
@Table(name = "ORDER_FLOW_STATUS_TRANS_SQL", schema = "APP_PBE")
public class OrderFlowStatusTransSql extends GryfEntity {

    //FIELDS

    @Id
    @NotNull
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column(name = "TYPE")
    @Enumerated(value = EnumType.STRING)
    private OrderFlowStatusTransSqlType type;

    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "SQL")
    private String sql;

    @JsonBackReference("orderFlowStatusTransSqlList")
    @JoinColumns({
        @JoinColumn(name = "ORDER_FLOW_ID", referencedColumnName = "ORDER_FLOW_ID"),
        @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID"),
        @JoinColumn(name = "ACTION_ID", referencedColumnName = "ACTION_ID")})
    @ManyToOne(optional = false)
    private OrderFlowStatusTransition orderFlowStatusTransition;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderFlowStatusTransSqlType getType() {
        return type;
    }

    public void setType(OrderFlowStatusTransSqlType type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public OrderFlowStatusTransition getOrderFlowStatusTransition() {
        return orderFlowStatusTransition;
    }

    public void setOrderFlowStatusTransition(OrderFlowStatusTransition orderFlowStatusTransition) {
        this.orderFlowStatusTransition = orderFlowStatusTransition;
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
