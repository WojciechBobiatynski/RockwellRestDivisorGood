package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

/**
 * DTO for element type Varchar
 * <p>
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
@ToString
public class OrderElementAttrVDTO extends OrderElementDTO {

    //FIELDS

    private String valueVarchar;

    //GETTERS & SETTERS

    public String getValueVarchar() {
        return valueVarchar;
    }

    public void setValueVarchar(String valueVarchar) {
        this.valueVarchar = valueVarchar;
    }
}
