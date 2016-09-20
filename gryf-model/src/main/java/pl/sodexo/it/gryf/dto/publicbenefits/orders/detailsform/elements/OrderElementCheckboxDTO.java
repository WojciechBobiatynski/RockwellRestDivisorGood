package pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements;

/**
 * DTO for element type Checkbox
 * <p>
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
public class OrderElementCheckboxDTO extends OrderElementDTO {

    //FIELDS

    private Boolean isSelected;

    //CONSTRUCTORS


    public OrderElementCheckboxDTO() {
    }

    public OrderElementCheckboxDTO(OrderElementDTOBuilder builder) {
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
