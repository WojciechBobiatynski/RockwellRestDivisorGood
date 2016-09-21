package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import java.util.Date;

/**
 * DTO for element type Date
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
public class OrderElementAttrDDTO extends OrderElementDTO {

    //FIELDS

    private Date valueDate;

    //CONSTRUCTORS

    public OrderElementAttrDDTO(){
    }

    public OrderElementAttrDDTO(OrderElementDTOBuilder builder) {
        super(builder);
        this.valueDate = builder.getElement().getValueDate();
    }

    //GETTERS & SETTERS

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }
}
