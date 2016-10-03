package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.transitions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by akmiecinski on 2016-10-03.
 */
@ToString
public class OrderFlowStatusTransitionPKDTO {

    @Getter
    @Setter
    private Long orderFlowId;

    @Getter
    @Setter
    private String statusId;

    @Getter
    @Setter
    private Long actionId;

}
