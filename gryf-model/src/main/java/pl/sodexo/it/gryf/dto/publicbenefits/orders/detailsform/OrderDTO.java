package pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform;

import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.transitions.OrderFlowTransitionDTO;

import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
public class OrderDTO {

    //PRIVATE FIELDS

    private Long id;

    private List<OrderElementDTO> elements;

    private List<OrderFlowTransitionDTO> actions;

    private Integer version;

    //CONSTRUCTORS

    private OrderDTO(){
    }

    private OrderDTO(Long id, List<OrderElementDTO> elements, List<OrderFlowTransitionDTO> actions, Integer version) {
        this.id = id;
        this.elements = elements;
        this.actions = actions;
        this.version = version;
    }

    //STATIC METHODS - CREATE

    public static OrderDTO create(Long id, List<OrderElementDTO> elements, List<OrderFlowTransitionDTO> actions, Integer version) {
        return new OrderDTO(id, elements, actions, version);
    }

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderElementDTO> getElements() {
        return elements;
    }

    public void setElements(List<OrderElementDTO> elements) {
        this.elements = elements;
    }

    public List<OrderFlowTransitionDTO> getActions() {
        return actions;
    }

    public void setActions(List<OrderFlowTransitionDTO> actions) {
        this.actions = actions;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
