package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action;

import lombok.ToString;

/**
 * Created by tomasz.bilski.ext on 2015-08-19.
 * 
 * Celem klasy jest przykazywanie elementu zamówienia przychodzącego z frontu do backendu
 * 
 */
@ToString
public class IncomingOrderElementDTO {

    //FIELDS

    private String elementTypeComponentName;

    private String data;

    //GETTERS & SETTERS

    public String getElementTypeComponentName() {
        return elementTypeComponentName;
    }

    public void setElementTypeComponentName(String elementTypeComponentName) {
        this.elementTypeComponentName = elementTypeComponentName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
