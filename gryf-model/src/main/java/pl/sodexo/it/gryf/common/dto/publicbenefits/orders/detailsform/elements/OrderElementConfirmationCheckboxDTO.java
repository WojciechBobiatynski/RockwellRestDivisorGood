package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

/**
 * DTO for element type ConfirmationCheckbox, a checkbox which must be selected (true) to be considered filled with data (completed)
 * <p>
 * 
 */
@ToString
public class OrderElementConfirmationCheckboxDTO extends OrderElementDTO {

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
