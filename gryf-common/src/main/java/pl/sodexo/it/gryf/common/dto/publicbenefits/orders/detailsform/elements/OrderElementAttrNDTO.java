package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

import java.math.BigDecimal;

/**
 * DTO for element type Number
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
@ToString
public class OrderElementAttrNDTO extends OrderElementDTO {

    //FIELDS

    private BigDecimal valueNumber;

    //GETTERS

    public BigDecimal getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(BigDecimal valueNumber) {
        this.valueNumber = valueNumber;
    }
}
