package pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementConfirmationCheckboxDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.OrderElementBaseService;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * Serwis obsługujący element type confirmation checkbox - pole wyboru, które musi być zaznaczone aby było uznane za "wypełnione".
 */
@Service
@Transactional
public class OrderElementConfirmationCheckboxService extends OrderElementBaseService<OrderElementConfirmationCheckboxDTO> {

    @Override
    public OrderElementConfirmationCheckboxDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementConfirmationCheckboxDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementConfirmationCheckboxDTO dto) {
        element.setValueVarchar(dto.getIsSelected() != null ? dto.getIsSelected().toString() : null);
        element.setCompletedDate(dto.getIsSelected() != null && dto.getIsSelected() ? new Date() : null);
    }

    //PROTECTED METHODS - OVERRIDE VALIDATION

    @Override
    protected boolean isValueInserted(OrderElement orderElement, OrderElementConfirmationCheckboxDTO dto){
        Boolean valOld = parseValue(orderElement.getValueVarchar());
        Boolean valNew = dto.getIsSelected();
        return valOld == null && valNew != null;
    }

    @Override
    protected boolean isValueUpdated(OrderElement orderElement, OrderElementConfirmationCheckboxDTO dto){
        Boolean valOld = parseValue(orderElement.getValueVarchar());
        Boolean valNew = dto.getIsSelected();
        return !Objects.equals(valOld, valNew);
    }

    @Override
    protected boolean isValueFilled(OrderElement orderElement, OrderElementConfirmationCheckboxDTO dto){
        Boolean valNew = dto.getIsSelected();
        return valNew != null;
    }

    @Override
    protected void addViolation(List<EntityConstraintViolation> violations, OrderElementConfirmationCheckboxDTO dto, String message){
        violations.add(new EntityConstraintViolation(dto.getOrderFlowElementId(), message, dto.getIsSelected()));
    }

    //PRIVATE METHODS

    private Boolean parseValue(String value){
        if(value == null){
            return null;
        }
        return Boolean.valueOf(value);
    }

    private boolean isValueChange(String valOld, Boolean valNew){
        return (valNew == null && valOld != null) ||
                (valNew != null && !Objects.equals(valOld, valNew.toString()));
    }
}
