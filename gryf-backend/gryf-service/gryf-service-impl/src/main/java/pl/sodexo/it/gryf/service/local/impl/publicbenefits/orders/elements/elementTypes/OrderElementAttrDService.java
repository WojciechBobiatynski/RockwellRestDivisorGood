package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementAttrDDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
@Service
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class OrderElementAttrDService extends OrderElementBaseService<OrderElementAttrDDTO> {

    @Override
    public OrderElementAttrDDTO createElement(OrderElementDTOBuilder builder) {
        return new OrderElementAttrDDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementAttrDDTO dto) {
        updateValue(element, dto.getValueDate());
    }

    public void updateValue(OrderElement element, Date date) {
        element.setValueDate(date);
        element.setCompletedDate(date != null ? new Date() : null);
    }

    //PROTECTED METHODS - OVERRIDE VALIDATION

    @Override
    protected boolean isValueInserted(OrderElement orderElement, OrderElementAttrDDTO dto){
        Date valOld = orderElement.getValueDate();
        Date valNew = dto.getValueDate();
        return valOld == null && valNew != null;
    }

    @Override
    protected boolean isValueUpdated(OrderElement orderElement, OrderElementAttrDDTO dto){
        Date valOld = orderElement.getValueDate();
        Date valNew = dto.getValueDate();
        return !Objects.equals(valOld, valNew);
    }

    @Override
    protected boolean isValueFilled(OrderElement orderElement, OrderElementAttrDDTO dto){
        Date valNew = dto.getValueDate();
        return valNew != null;
    }

    @Override
    protected void addViolation(List<EntityConstraintViolation> violations, OrderElementAttrDDTO dto, String message){
        violations.add(new EntityConstraintViolation(dto.getOrderFlowElementId(), message, dto.getValueDate()));
    }

}
