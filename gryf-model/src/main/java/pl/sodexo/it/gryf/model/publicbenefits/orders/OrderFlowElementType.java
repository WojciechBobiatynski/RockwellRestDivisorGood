/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.orders;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "ORDER_FLOW_ELEMENT_TYPES", schema = "APP_PBE")
public class OrderFlowElementType extends GryfEntity {

    /**
     * enum zawiera wybrane typy elementów niezbędne do sprawdzania po ID przy procesowaniu zamówień 
     */
    public enum ElementType{
        /**
         * Confirmation Checkbox - specjalny checkbox, który musi być zaznaczony aby był traktowany jako uzupełniony
         */
        CONFCHKBOX("Confirmation Checkbox");
        //FIELDS
        private String label;
        //CONSTRUCTORS
        private ElementType(String label){
            this.label = label;
        }    
    }
    
    //STATIC FIELDS - COMPONENT NAME FIELDS
    private static final String ORDER_ELEMENT_DTO_PACKAGE_PREFIX = "pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.";
    private static final String ORDER_ELEMENT_DTO_QUALIFIED_CLASS_PREFIX = "OrderElement";
    private static final String ORDER_ELEMENT_DTO_QUALIFIED_CLASS_SUFFIX = "DTO";
    private static final String ORDER_ELEMENT_SERVICE_PREFIX = "orderElement";
    private static final String ORDER_ELEMENT_SERVICE_SUFFIX = "Service";

    //FIELDS

    @Id
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ID")
    private String id;

    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "COMPONENT_NAME")
    private String componentName;

    @JsonManagedReference("orderFlowElements")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderFlowElementType")
    private List<OrderFlowElement> orderFlowElements;

    //GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    //LIST METHODS

    private List<OrderFlowElement> getInitializedOrderFlowElements() {
        if (orderFlowElements == null)
            orderFlowElements = new ArrayList<>();
        return orderFlowElements;
    }

    public List<OrderFlowElement> getOrderFlowElements() {
        return Collections.unmodifiableList(getInitializedOrderFlowElements());
    }

    //PUBLIC METHODS - COMPONENT METHODS

    public String getDtoClassName(){
        return getDtoClassName(componentName);
    }

    public String getServiceBeanName(){
        return getServiceBeanName(componentName);
    }

    public static String getDtoClassName(String value){
        return (value != null) ? (ORDER_ELEMENT_DTO_PACKAGE_PREFIX + ORDER_ELEMENT_DTO_QUALIFIED_CLASS_PREFIX +
                                    value + ORDER_ELEMENT_DTO_QUALIFIED_CLASS_SUFFIX) : null;
    }

    public static String getServiceBeanName(String value){
        return (value != null) ? (ORDER_ELEMENT_SERVICE_PREFIX + value + ORDER_ELEMENT_SERVICE_SUFFIX) : null;
    }

    //PUBLIC METHODS - HASH CODE & EQUALS

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
        return Objects.equals(id, ((OrderFlowElementType) o).id);
    }

    @Override
    public String toString() {
        return "pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementType[ id=" + id + " ]";
    }

}
