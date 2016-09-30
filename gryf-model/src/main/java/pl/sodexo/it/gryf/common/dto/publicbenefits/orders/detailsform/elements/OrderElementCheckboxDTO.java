package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

/**
 * DTO for element type Checkbox
 * <p>
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
@ToString
public class OrderElementCheckboxDTO extends OrderElementDTO {

    //FIELDS

    private Boolean isSelected;

    //GETTERS & SETTERS

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }
}
