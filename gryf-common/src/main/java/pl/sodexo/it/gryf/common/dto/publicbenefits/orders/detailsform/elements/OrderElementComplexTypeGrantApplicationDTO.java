package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-31.
 */
@ToString
public class OrderElementComplexTypeGrantApplicationDTO extends OrderElementDTO {

    //FIELDS
    private Long grantApplicationId;

    //GETTERS & SETTERS

    public Long getGrantApplicationId() {
        return grantApplicationId;
    }

    public void setGrantApplicationId(Long grantApplicationId) {
        this.grantApplicationId = grantApplicationId;
    }
}
