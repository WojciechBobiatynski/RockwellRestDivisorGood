package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-31.
 */
public class OrderElementComplexTypeGrantApplicationDTO extends OrderElementDTO {

    //FIELDS
    private Long grantApplicationId;

    //CONSTRUCTORS

    public OrderElementComplexTypeGrantApplicationDTO() {
    }

    public OrderElementComplexTypeGrantApplicationDTO(OrderElementDTOBuilder builder) {
        super(builder);
        grantApplicationId = builder.getOrder().getApplication().getId();
    }

    //GETTERS & SETTERS

    public Long getGrantApplicationId() {
        return grantApplicationId;
    }

    public void setGrantApplicationId(Long grantApplicationId) {
        this.grantApplicationId = grantApplicationId;
    }
}
