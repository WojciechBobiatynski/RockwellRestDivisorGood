package pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementAttrVDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;
import pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.OrderElementBaseService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
@Service
public class OrderElementAttrVService extends OrderElementBaseService<OrderElementAttrVDTO> {

    //PUBLIC METHODS

    @Override
    public OrderElementAttrVDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementAttrVDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementAttrVDTO dto) {
        updateValue(element, dto.getValueVarchar());
    }

    public void updateValue(OrderElement element, String value) {
        element.setValueVarchar(value);
        element.setCompletedDate(!StringUtils.isEmpty(value) ? new Date() : null);
    }

    //PROTECTED METHODS - OVERRIDE VALIDATION

    @Override
    protected boolean isValueInserted(OrderElement orderElement, OrderElementAttrVDTO dto){
        String valOld = orderElement.getValueVarchar();
        String valNew = dto.getValueVarchar();
        return StringUtils.isEmpty(valOld) && !StringUtils.isEmpty(valNew);
    }

    @Override
    protected boolean isValueUpdated(OrderElement orderElement, OrderElementAttrVDTO dto){
        String valOld = orderElement.getValueVarchar();
        String valNew = dto.getValueVarchar();
        return !Objects.equals(valOld, valNew);
    }

    @Override
    protected boolean isValueFilled(OrderElement orderElement, OrderElementAttrVDTO dto){
        String valNew = dto.getValueVarchar();
        return !StringUtils.isEmpty(valNew);
    }

    @Override
    protected void addViolation(List<EntityConstraintViolation> violations, OrderElementAttrVDTO dto, String message){
        violations.add(new EntityConstraintViolation(dto.getOrderFlowElementId(), message, dto.getValueVarchar()));
    }

    @Override
    protected void validateCustom(List<EntityConstraintViolation> violations, OrderElement orderElement, OrderFlowElementInStatus orderFlowElementInStatus, OrderElementAttrVDTO dto) {
        OrderFlowElement orderFlowElement = orderFlowElementInStatus.getOrderFlowElement();

        //JEST WARTOSC
        String value = dto.getValueVarchar();
        if(!StringUtils.isEmpty(value)){

            //MAX LENGTH
            String maxLengthStr = orderFlowElement.getPropertyValue(OrderFlowElement.PARAM_MAX_LENGTH);
            if (!StringUtils.isEmpty(maxLengthStr)) {
                Integer maxLength = Integer.valueOf(maxLengthStr);
                if (value.length() > maxLength) {
                    addViolation(violations, dto, String.format("Pole '%s' może mieć maksymalnie 200 znaków", orderFlowElement.getElementName()));
                }
            }

        }
    }
}
