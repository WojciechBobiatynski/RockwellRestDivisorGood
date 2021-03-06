package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements;

/**
 * Created by tomasz.bilski.ext on 2015-09-21.
 */

import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementClob;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionElementFlag;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.OrderElementService;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;

public abstract class OrderElementBaseClobService<T extends OrderElementDTO>  implements OrderElementService<T> {

    //FIELDS

    protected Class<T> dtoClass;

    @Autowired
    private SecurityChecker securityChecker;

    //CONSTRUCTORS

    public OrderElementBaseClobService(){
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.dtoClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    //PUBLIC METHODS

    @Override
    public void updateValue(OrderElement element, T dto) {

        //SPARSOWANIE
        String dtoStr = JsonMapperUtils.writeValueAsString(dto);

        //ZAPISANIE
        OrderElementClob clob = element.getClob();
        if(clob == null){
            clob = new OrderElementClob();
            clob.setOrderElement(element);
            clob.setOrderElementId(element.getId());
            element.setClob(clob);
        }
        clob.setValueClob(dtoStr);
    }

    @Override
    public void validate(List<EntityConstraintViolation> violations, OrderElement orderElement,
                         OrderFlowElementInStatus orderFlowElementInStatus, OrderFlowStatusTransitionElementFlag orderFlowStatusTransitionElementFlag, T dto) {

        T oldDto = getOldValue(orderElement);

        //VALIDATE FLAGS
        validateFlags(violations, oldDto, orderFlowElementInStatus, orderFlowStatusTransitionElementFlag, dto);

        //WALICACJA NA POSTAWIE PARAMS??W
        validateCustom(violations, orderElement, orderFlowElementInStatus, dto);

        //WALIDACJA PRAWA U??YTKOWNIKA
        validatePrivilege(violations, oldDto, orderFlowElementInStatus, dto);
    }

    //PROTECTED METHODS - VALIDATE

    protected void validateFlags(List<EntityConstraintViolation> violations, T oldDto,
                                 OrderFlowElementInStatus orderFlowElementInStatus, OrderFlowStatusTransitionElementFlag orderFlowStatusTransitionElementFlag, T dto){

        //BLAD GDY - BRAK FLAGI 'I' ORAZ WARTOSC ZOSTALA WYPELNIONA
        if(!orderFlowElementInStatus.isInsertable() && !isTransitionFlagInsertable(orderFlowStatusTransitionElementFlag)){
            if(isValueInserted(oldDto, dto)){
                addViolation(violations, dto, String.format("W danym przej??ciu nie mo??na wype??nia?? p??l elemntu '%s'", dto.getName()));
            }
        }

        //BLAD GDY - BRAK FLAGI 'U' ORAZ WARTOSC ZOSTALA ZMIENIONA
        if(!orderFlowElementInStatus.isUpdatable() && !isTransitionFlagUpdatable(orderFlowStatusTransitionElementFlag)){
            if(isValueUpdated(oldDto, dto)){
                addViolation(violations, dto, String.format("W danym przej??ciu nie mo??na zmienia?? p??l elemntu '%s'", dto.getName()));
            }
        }

        //BLAD GDY - JEST FLAGA 'M' ORAZ NIE ZOSTALA WYPELNIONA
        if(orderFlowElementInStatus.isMandatory() || isTransitionFlagMandatory(orderFlowStatusTransitionElementFlag)){
            if(!isValueFilled(dto)){
                addViolation(violations, dto, String.format("W danym przej??ciu nale??y wype??ni?? pola elementu '%s'", dto.getName()));
            }
        }
    }

    protected void validateCustom(List<EntityConstraintViolation> violations, OrderElement orderElement, OrderFlowElementInStatus orderFlowElementInStatus, T dto){
    }

    protected void validatePrivilege(List<EntityConstraintViolation> violations, T oldDto, OrderFlowElementInStatus orderFlowElementInStatus, T dto){
        String privilege = orderFlowElementInStatus.getAugIdRequired();
        if(!GryfStringUtils.isEmpty(privilege)){
            if(isValueUpdated(oldDto, dto)){
                if (!securityChecker.hasPrivilege(privilege)) {
                    addViolation(violations, dto, String.format("Nie masz uprawnie?? do edycji p??l elementu '%s'", dto.getName()));
                }
            }
        }
    }

    //PROTECTED METHODS - IUM - DTO

    protected abstract boolean isValueInserted(T oldDto, T newDto);

    protected abstract boolean isValueUpdated(T oldDto, T newDto);

    protected abstract boolean isValueFilled(T newDto);

    //PROTECTED METHODS - IUM - STRING

    protected boolean isValueInserted(String oldValue, String newValue){
        return GryfStringUtils.isEmpty(oldValue) && !GryfStringUtils.isEmpty(newValue);
    }

    protected boolean isValueUpdated(String oldValue, String newValue){
        return !Objects.equals(oldValue, newValue);
    }

    protected boolean isValueFilled(String newVal){
        return !GryfStringUtils.isEmpty(newVal);
    }

    //PRIVATE METHODS - TRANSITION FLAGS

    private boolean isTransitionFlagInsertable(OrderFlowStatusTransitionElementFlag orderFlowStatusTransitionElementFlag){
        return orderFlowStatusTransitionElementFlag != null && orderFlowStatusTransitionElementFlag.isInsertable();
    }

    private boolean isTransitionFlagUpdatable(OrderFlowStatusTransitionElementFlag orderFlowStatusTransitionElementFlag){
        return orderFlowStatusTransitionElementFlag != null && orderFlowStatusTransitionElementFlag.isUpdatable();
    }

    private boolean isTransitionFlagMandatory(OrderFlowStatusTransitionElementFlag orderFlowStatusTransitionElementFlag){
        return orderFlowStatusTransitionElementFlag != null && orderFlowStatusTransitionElementFlag.isMandatory();
    }

    //PROTECTED METHODS

    protected T getOldValue(OrderElement orderElement){
        if(orderElement.getClob() != null){
            String valueClob =  orderElement.getClob().getValueClob();
            return(T) JsonMapperUtils.readValue(valueClob, dtoClass);
        }
        return null;
    }

    protected void addViolation(List<EntityConstraintViolation> violations, T dto, String message){
        violations.add(new EntityConstraintViolation(dto.getOrderFlowElementId(), message, null));
    }

}
