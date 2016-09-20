package pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dto.DictionaryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementComboboxDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElement;
import pl.sodexo.it.gryf.root.repository.DictionaryRepository;
import pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
@Service
public class OrderElementComboboxService extends OrderElementBaseService<OrderElementComboboxDTO> {

    //FIELDS

    @Autowired
    private DictionaryRepository dictionaryRepository;

    //PUBLIC METHODS

    @Override
    public OrderElementComboboxDTO createElement(OrderElementDTOBuilder builder) {
        OrderFlowElement orderFlowElement = builder.getOrderFlowElement();
        String nativeSql = orderFlowElement.getPropertyValue(OrderFlowElement.PARAM_SQL_EXPRESSION);


        List<DictionaryDTO> dictionaries =  !StringUtils.isEmpty(nativeSql) ?
                                        dictionaryRepository.findDictionaries(nativeSql) : new ArrayList<DictionaryDTO>();
        return new OrderElementComboboxDTO(builder, dictionaries);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComboboxDTO dto) {
        updateValue(element, dto.getSelectedItem());
    }

    public void updateValue(OrderElement element, DictionaryDTO value) {
        element.setValueVarchar(value != null ? (String)value.getId() : null);
        element.setCompletedDate(value != null ? new Date() : null);
    }

    //PROTECTED METHODS - OVERRIDE VALIDATION

    @Override
    protected boolean isValueInserted(OrderElement orderElement, OrderElementComboboxDTO dto){
        String valOld = orderElement.getValueVarchar();
        DictionaryDTO valNew = dto.getSelectedItem();
        return valOld == null && valNew != null;
    }

    @Override
    protected boolean isValueUpdated(OrderElement orderElement, OrderElementComboboxDTO dto){
        String valOld = orderElement.getValueVarchar();
        DictionaryDTO valNew = dto.getSelectedItem();
        return isValueChange(valOld, valNew);
    }

    @Override
    protected boolean isValueFilled(OrderElement orderElement, OrderElementComboboxDTO dto){
        DictionaryDTO valNew = dto.getSelectedItem();
        return valNew != null;
    }

    @Override
    protected void addViolation(List<EntityConstraintViolation> violations, OrderElementComboboxDTO dto, String message){
        violations.add(new EntityConstraintViolation(dto.getOrderFlowElementId(), message,
                            dto.getSelectedItem() != null ? dto.getSelectedItem().getName() : null));
    }

    //PRIVATE METHODS

    private boolean isValueChange(String valOld, DictionaryDTO valNew){
        return (valNew == null && valOld != null) ||
                (valNew != null && !Objects.equals(valOld, valNew.getId()));
    }

}
