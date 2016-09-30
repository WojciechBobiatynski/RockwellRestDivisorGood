package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

import java.util.Date;

/**
 * Basic class for Element DTO. Celem klasy (klas dziedziczących) jest przekazywanie danych elementu zamówienia z backendu do frontu  
 * Created by Michal.CHWEDCZUK.ext on 2015-07-24.
 */
@ToString
public abstract class OrderElementDTO {

    //FIELDS

    private Long id;

    private String orderFlowElementId;

    private String name;

    private String elementTypeComponentName;

    private String elementTypeParams;

    private String flags;

    private String privilege;

    private Date requiredDate;

    private Date completedDate;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderFlowElementId() {
        return orderFlowElementId;
    }

    public void setOrderFlowElementId(String orderFlowElementId) {
        this.orderFlowElementId = orderFlowElementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElementTypeComponentName() {
        return elementTypeComponentName;
    }

    public void setElementTypeComponentName(String elementTypeComponentName) {
        this.elementTypeComponentName = elementTypeComponentName;
    }

    public String getElementTypeParams() {
        return elementTypeParams;
    }

    public void setElementTypeParams(String elementTypeParams) {
        this.elementTypeParams = elementTypeParams;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
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
}
