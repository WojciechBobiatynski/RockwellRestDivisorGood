package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementCheckboxDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.orders.action.OrderElementDTOProvider;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
@Service
public class OrderElementCheckboxService extends OrderElementBaseService<OrderElementCheckboxDTO> {

    @Override
    public OrderElementCheckboxDTO createElement(OrderElementDTOBuilder builder) {
        return OrderElementDTOProvider.createOrderElementCheckboxDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementCheckboxDTO dto) {
        updateValue(element, dto.getIsSelected());
    }

    public void updateValue(OrderElement element, Boolean  isSelected) {
        element.setValueVarchar(isSelected != null ? isSelected.toString() : null);
        element.setCompletedDate(isSelected != null ? new Date() : null);
    }

    //PROTECTED METHODS - OVERRIDE VALIDATION

    @Override
    protected boolean isValueInserted(OrderElement orderElement, OrderElementCheckboxDTO dto){
        Boolean valOld = parseValue(orderElement.getValueVarchar());
        Boolean valNew = dto.getIsSelected();
        return valOld == null && valNew != null;
    }

    @Override
    protected boolean isValueUpdated(OrderElement orderElement, OrderElementCheckboxDTO dto){
        Boolean valOld = parseValue(orderElement.getValueVarchar());
        Boolean valNew = dto.getIsSelected();
        return !Objects.equals(valOld, valNew);
    }

    @Override
    protected boolean isValueFilled(OrderElement orderElement, OrderElementCheckboxDTO dto){
        Boolean valNew = dto.getIsSelected();
        return valNew != null;
    }

    @Override
    protected void addViolation(List<EntityConstraintViolation> violations, OrderElementCheckboxDTO dto, String message){
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
