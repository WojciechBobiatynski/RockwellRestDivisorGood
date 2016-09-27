/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.orders;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.GryfEntity;

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
@Entity
@Table(name = "ORDER_FLOWS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = OrderFlow.FIND_BY_GRANT_APPLICATION_VERSION_IN_DATE, query = "select distinct offgav.orderFlow from OrderFlowForGrantApplicationVersion offgav " +
                                                                                        "where offgav.grantApplicationVersion.id = :versionId " +
                                                                                        "and (offgav.dateFrom is null or offgav.dateFrom <= :date) " +
                                                                                        "and (offgav.dateTo is null or :date <= offgav.dateTo)")})
public class OrderFlow extends GryfEntity {

    //STATIC FIELDS - COMPONENT NAME FIELDS

    private static final String ORDER_FLOW_SERVICE_PREFIX = "orderFlow";
    private static final String ORDER_FLOW_SERVICE_SUFFIX = "Service";

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_GRANT_APPLICATION_VERSION_IN_DATE = "OrderFlow.findByGrantApplicationVersionInDate";

    //FIELDS

    @Id
    @NotNull
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NAME")
    private String name;

    @JsonManagedReference("initialStatus")
    @JoinColumn(name = "INITIAL_STATUS_ID", referencedColumnName = "STATUS_ID")
    @ManyToOne(optional = false)
    private OrderFlowStatus initialStatus;

    @JsonManagedReference("orders")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderFlow")
    private List<Order> orders;

    @JsonManagedReference("orderFlowStatusTransitions")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderFlow")
    private List<OrderFlowStatusTransition> orderFlowStatusTransitions;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderFlowStatus getInitialStatus() {
        return initialStatus;
    }

    public void setInitialStatus(OrderFlowStatus initialStatusId) {
        this.initialStatus = initialStatusId;
    }

    //LIST METHODS

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

    //PUBLIC METHODS - COMPONENT METHODS


    public String getServiceBeanName(){
        return getServiceBeanName(id);
    }

    public static String getServiceBeanName(Long value){
        return (value != null) ? (ORDER_FLOW_SERVICE_PREFIX +
                ((value < 0) ? "Test" : "") + Math.abs(value) +
                ORDER_FLOW_SERVICE_SUFFIX) : null;
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
        return Objects.equals(id, ((OrderFlow) o).id);
    }

    @Override
    public String toString() {
        return "pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow[ id=" + id + " ]";
    }

}
