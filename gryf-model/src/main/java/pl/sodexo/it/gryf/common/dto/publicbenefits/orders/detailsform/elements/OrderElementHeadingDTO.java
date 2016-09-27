package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

/**
 * DTO dla nagłówka 
 * 
 */
@ToString
public class OrderElementHeadingDTO extends OrderElementDTO {

    //FIELDS

    //CONSTRUCTORS

    public OrderElementHeadingDTO(){
    }

    public OrderElementHeadingDTO(OrderElementDTOBuilder builder) {
        super(builder);
    }

    //GETTERS & SETTERS
}
