package pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements;

/**
 * DTO for element type Varchar
 * <p>
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
public class OrderElementAttrVDTO extends OrderElementDTO {

    //FIELDS

    private String valueVarchar;

    //CONSTRUCTORS


    public OrderElementAttrVDTO() {
    }

    public OrderElementAttrVDTO(OrderElementDTOBuilder builder) {
        super(builder);
        this.valueVarchar = builder.getElement().getValueVarchar();
    }

    //GETTERS & SETTERS

    public String getValueVarchar() {
        return valueVarchar;
    }

    public void setValueVarchar(String valueVarchar) {
        this.valueVarchar = valueVarchar;
    }
}
