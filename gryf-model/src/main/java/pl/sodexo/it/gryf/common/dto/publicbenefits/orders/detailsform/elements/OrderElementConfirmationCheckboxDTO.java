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

    //CONSTRUCTORS


    public OrderElementConfirmationCheckboxDTO() {
    }

    public OrderElementConfirmationCheckboxDTO(OrderElementDTOBuilder builder) {
        super(builder);
        this.isSelected = Boolean.valueOf(builder.getElement().getValueVarchar());
    }

    //GETTERS & SETTERS

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }
}
