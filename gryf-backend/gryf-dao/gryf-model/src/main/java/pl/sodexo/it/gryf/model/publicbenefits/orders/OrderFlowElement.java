/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.orders;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * Type of elements available for orders
 * @author Michal.CHWEDCZUK.ext
 */
@ToString(exclude = {"orderFlowElementType", "orderElements", "orderFlowElementInStatuses", "elementTypeParamMap"})
@Entity
@Table(name = "ORDER_FLOW_ELEMENTS", schema = "APP_PBE")
public class OrderFlowElement extends GryfEntity {

    //STATIC FIELDS - PARAMS

    public static final String PARAM_MIN_VALUE = "MIN_VALUE";
    public static final String PARAM_MAX_VALUE = "MAX_VALUE";
    public static final String PARAM_MAX_LENGTH = "MAX_LENGTH";
    public static final String PARAM_SQL_EXPRESSION = "SQL_EXPRESSION";
    /**
     * Parametr dedykowany do elementu typu C_EMAIL określający bean serwisowy generujący właściwy e-mail
     */
    public static final String EMAIL_SERVICE_BEAN = "EMAIL_SERVICE_BEAN";

    //FIELDS

    @Id
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ELEMENT_ID")
    private String elementId;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ELEMENT_NAME")
    private String elementName;

    @Size(max = 500)
    @Column(name = "ELEMENT_TYPE_PARAMS")
    private String elementTypeParams;

    @JsonBackReference("orderFlowElements")
    @JoinColumn(name = "ELEMENT_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrderFlowElementType orderFlowElementType;

    @Size(max = 500)
    @Column(name = "ELEMENT_DESCRIPTION")
    private String elementDescription;

    @JsonManagedReference("orderElements")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderFlowElement")
    private List<OrderElement> orderElements;

    @JsonManagedReference("orderFlowElementInStatuses")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderFlowElement")
    private List<OrderFlowElementInStatus> orderFlowElementInStatuses;

    @Transient
    private Map<String, String> elementTypeParamMap;

    //GETTERS & SETTERS

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementTypeParams() {
        return elementTypeParams;
    }

    public void setElementTypeParams(String elementTypeParams) {
        this.elementTypeParams = elementTypeParams;
    }

    public OrderFlowElementType getOrderFlowElementType() {
        return orderFlowElementType;
    }

    public void setOrderFlowElementType(OrderFlowElementType orderFlowElementType) {
        this.orderFlowElementType = orderFlowElementType;
    }

    public String getElementDescription() {
        return elementDescription;
    }

    public void setElementDescription(String elementDescription) {
        this.elementDescription = elementDescription;
    }

    //LIST METHODS

    private List<OrderElement> getInitializedOrderElements() {
        if (orderElements == null)
            orderElements = new ArrayList<>();
        return orderElements;
    }

    public List<OrderElement> getOrderElements() {
        return Collections.unmodifiableList(getInitializedOrderElements());
    }

    private List<OrderFlowElementInStatus> getInitializedOrderFlowElementInStatuses() {
        if (orderFlowElementInStatuses == null)
            orderFlowElementInStatuses = new ArrayList<>();
        return orderFlowElementInStatuses;
    }

    public List<OrderFlowElementInStatus> getOrderFlowElementInStatuses() {
        return Collections.unmodifiableList(getInitializedOrderFlowElementInStatuses());
    }

    //PUBLIC METHODS

    public String getPropertyValue(String key){
        if(elementTypeParamMap == null){
            elementTypeParamMap = createElementTypeParamMap();
        }
        return elementTypeParamMap.get(key);
    }

    /**
     * Metoda tworzy mapą par klucz-wartość na podstawie pola elementTypeParams. Najpier dzieli wartosc z pola
     * na podstawie znaku ';' nastyenie dla każdego elementu to co jest po prawej stronie znaku '=' jest kluczem
     * to co po lewej stronie znaku '=' jest wartością.
     * @return
     */
    private Map<String, String> createElementTypeParamMap(){
        Map<String, String> result = new HashMap<>();
        if(!StringUtils.isEmpty(elementTypeParams)){
            String[] keyValueStr = elementTypeParams.split(";");
            for (int i = 0; i < keyValueStr.length; i++) {
                if(!StringUtils.isEmpty(keyValueStr[i])) {
                    int equalIndex = keyValueStr[i].indexOf('=');
                    if (equalIndex <= 0) {
                        throw new RuntimeException(String.format("Błąd konfiguracji - nie udało się sparsować " +
                                "pola elementTypeParam dla elementu '%s'", elementId));
                    }
                    String key = keyValueStr[i].substring(0, equalIndex);
                    String value = keyValueStr[i].substring(equalIndex + 1);
                    result.put(key.trim(), value.trim());
                }
            }
        }
        return result;
    }

    //HASH CODE & EQUALS

    @Override
    public int hashCode() {
        return Objects.hashCode(elementId) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(elementId, ((OrderFlowElement) o).elementId);
    }
}
