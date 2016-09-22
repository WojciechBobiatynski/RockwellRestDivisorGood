package pl.sodexo.it.gryf.service.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementAttrNDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;
import pl.sodexo.it.gryf.service.impl.publicbenefits.orders.elements.OrderElementBaseService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
@Service
public class OrderElementAttrNService extends OrderElementBaseService<OrderElementAttrNDTO> {

    @Override
    public OrderElementAttrNDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementAttrNDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementAttrNDTO dto) {
        updateValue(element, dto.getValueNumber());
    }

    public void updateValue(OrderElement element, BigDecimal value) {
        element.setValueNumber(value);
        element.setCompletedDate(value != null ? new Date() : null);
    }

    //PROTECTED METHODS - OVERRIDE VALIDATION

    @Override
    protected boolean isValueInserted(OrderElement orderElement, OrderElementAttrNDTO dto){
        BigDecimal valOld = orderElement.getValueNumber();
        BigDecimal valNew = dto.getValueNumber();
        return valOld == null && valNew != null;
    }

    @Override
    protected boolean isValueUpdated(OrderElement orderElement, OrderElementAttrNDTO dto){
        BigDecimal valOld = orderElement.getValueNumber();
        BigDecimal valNew = dto.getValueNumber();
        return !Objects.equals(valOld, valNew);
    }

    @Override
    protected boolean isValueFilled(OrderElement orderElement, OrderElementAttrNDTO dto){
        BigDecimal valNew = dto.getValueNumber();
        return valNew != null;
    }

    @Override
    protected void addViolation(List<EntityConstraintViolation> violations, OrderElementAttrNDTO dto, String message){
        violations.add(new EntityConstraintViolation(dto.getOrderFlowElementId(), message, dto.getValueNumber()));
    }

    @Override
    protected void validateCustom(List<EntityConstraintViolation> violations, OrderElement orderElement, OrderFlowElementInStatus orderFlowElementInStatus, OrderElementAttrNDTO dto) {
        OrderFlowElement orderFlowElement = orderFlowElementInStatus.getOrderFlowElement();

        //JEST WARTOSC
        BigDecimal value = dto.getValueNumber();
        if(value != null){

            //MIN VALUE
            String minValueStr = orderFlowElement.getPropertyValue(OrderFlowElement.PARAM_MIN_VALUE);
            if (!StringUtils.isEmpty(minValueStr)) {
                BigDecimal minValue = new BigDecimal(minValueStr);
                if(minValue.compareTo(value) > 0){
                    addViolation(violations, dto, String.format("Minimalna wartość w polu '%s' wynoski %s",
                            orderFlowElement.getElementName(), minValue));
                }
            }

            //MAX VALUE
            String maxValueStr = orderFlowElement.getPropertyValue(OrderFlowElement.PARAM_MAX_VALUE);
            if (!StringUtils.isEmpty(maxValueStr)) {
                BigDecimal maxValue = new BigDecimal(maxValueStr);
                if(value.compareTo(maxValue) > 0){
                    addViolation(violations, dto, String.format("Maksymalna wartość w polu '%s' wynoski %s",
                            orderFlowElement.getElementName(), maxValue));
                }
            }
        }
    }
}
