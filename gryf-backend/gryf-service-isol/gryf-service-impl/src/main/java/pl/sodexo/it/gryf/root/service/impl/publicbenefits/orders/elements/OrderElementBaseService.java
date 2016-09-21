package pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.elements;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;
import pl.sodexo.it.gryf.root.service.local.publicbenefits.orders.elements.OrderElementService;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-21.
 */
public abstract class OrderElementBaseService<T extends OrderElementDTO> implements OrderElementService<T> {

    //FIELDS

    @Autowired
    private SecurityCheckerService securityCheckerService;

    //PUBLIC METHODS

    @Override
    public void validate(List<EntityConstraintViolation> violations, OrderElement orderElement, OrderFlowElementInStatus orderFlowElementInStatus, T dto) {

        //VALIDATE FLAGS
        validateFlags(violations, orderElement, orderFlowElementInStatus, dto);

        //WALICACJA NA POSTAWIE PARAMSÓW
        validateCustom(violations, orderElement, orderFlowElementInStatus, dto);

        //WALIDACJA PRAWA UŻYTKOWNIKA
        validatePrivilege(violations, orderElement, orderFlowElementInStatus, dto);
    }

    //PROTECTED METHODS - VALIDATION

    /**
     * Waliduje poprawnosć flag.
     * @param violations lista błędów gdzie zostaja dodane ewentualne błędy
     * @param orderElement elemnt dla którego jest robiona walidacja
     * @param orderFlowElementInStatus element w statusie dla którego jest robiona walidacja
     * @param dto obiekt transferowy jaki przyszedł do zwalidowania
     */
    protected void validateFlags(List<EntityConstraintViolation> violations, OrderElement orderElement, OrderFlowElementInStatus orderFlowElementInStatus, T dto){

        //BLAD GDY - BRAK FLAGI 'I' ORAZ WARTOSC ZOSTALA WYPELNIONA
        if(!orderFlowElementInStatus.isInsertable()){
            if(isValueInserted(orderElement, dto)){
                addViolation(violations, dto, String.format("W danym statusie nie można wypełniać pola '%s'", dto.getName()));
            }
        }

        //BLAD GDY - BRAK FLAGI 'U' ORAZ WARTOSC ZOSTALA ZMIENIONA
        if(!orderFlowElementInStatus.isUpdatable()){
            if(isValueUpdated(orderElement, dto)){
                addViolation(violations, dto, String.format("W danym statusie nie można zmieniać pola '%s'", dto.getName()));
            }
        }

        //BLAD GDY - JEST FLAGA 'M' ORAZ NIE ZOSTALA WYPELNIONA
        if(orderFlowElementInStatus.isMandatory()){
            if(!isValueFilled(orderElement, dto)){
                addViolation(violations, dto, String.format("W danym statusie należy wypełnić pole '%s'", dto.getName()));
            }
        }
    }

    /**
     * Zwraca czy wartość zostala zinsertowana. Metoda powina byc nadwpisana w klasach pochodnych.
     * @param orderElement obiekt encji ze starą wartoscią
     * @param dto obiekt transerwoy z nowa wartoscia
     * @return
     */
    protected boolean isValueInserted(OrderElement orderElement, T dto){
        return false;
    }

    /**
     * Zwraca czy wartość zostala zmieniony. Metoda powina byc nadwpisana w klasach pochodnych.
     * @param orderElement obiekt encji ze starą wartoscią
     * @param dto obiekt transerwoy z nowa wartoscia
     * @return
     */
    protected boolean isValueUpdated(OrderElement orderElement, T dto){
        return false;
    }

    /**
     * Zwraca czy wartość zostala wypełniony. Metoda powina byc nadwpisana w klasach pochodnych.
     * @param orderElement obiekt encji ze starą wartoscią
     * @param dto obiekt transerwoy z nowa wartoscia
     * @return
     */
    protected boolean isValueFilled(OrderElement orderElement, T dto){
        return false;
    }

    /**
     * Dodaje bład do listy z błędami. Metoda powina byc nadwpisana w klasach pochodnych.
     * @param violations lista z błędami
     * @param dto objekt transferowy
     * @param message komunikat błędu
     */
    protected void addViolation(List<EntityConstraintViolation> violations, T dto, String message){
    }

    /**
     * Metodoa robiąza walidacjię na podstawie params. Metoda powina byc nadwpisana w klasach pochodnych.
     * @param violations lista z błędami
     * @param orderElement elemnt dla którego jest robiona walidacja
     * @param orderFlowElementInStatus element w statusie dla którego jest robiona walidacja
     * @param dto obiekt transferowy jaki przyszedł do zwalidowania
     */
    protected void validateCustom(List<EntityConstraintViolation> violations, OrderElement orderElement, OrderFlowElementInStatus orderFlowElementInStatus, T dto){
    }

    /**
     * Metoda robi waldacjie czy użytkownik ma uprawninia do edycji danego pola - na podstawie AugIdRequired w OrderFlowElementInStatus.
     * * @param violations lista z błędami
     * @param orderElement elemnt dla którego jest robiona walidacja
     * @param orderFlowElementInStatus element w statusie dla którego jest robiona walidacja
     * @param dto obiekt transferowy jaki przyszedł do zwalidowania
     */
    protected void validatePrivilege(List<EntityConstraintViolation> violations, OrderElement orderElement, OrderFlowElementInStatus orderFlowElementInStatus, T dto){
        String privilege = orderFlowElementInStatus.getAugIdRequired();
        if(!StringUtils.isEmpty(privilege)){
            if(isValueUpdated(orderElement, dto)){
                if(!securityCheckerService.hasPrivilege(privilege)){
                    addViolation(violations, dto, String.format("Nie masz uprawnień do edycji tego pola '%s'", dto.getName()));
                }
            }
        }
    }

}
