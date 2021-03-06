package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransitionElementFlag;

import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
public interface OrderElementService<T extends OrderElementDTO> {

    /**
     * Tworzy element o typie powiazanym ze sobą. Metoda powinna byc nadpisana w poszczególnych konkretnych serwiach.
     * @param builder obiekt buildera na podstawie którego tworzony jest objekt dto
     * @return objekt dto
     */
    T createElement(OrderElementDTOBuilder builder);

    /**
     * Metoda uaktualniajaca wartość w odpowiednim polu obiektu OrderElement.
     * Metoda powinna byc nadpisana w poszczególnych konkretnych serwiach.
     * @param element objekt encyjny w którym zapisujemy dane
     * @param dto objekt z danymi
     */
    void updateValue(OrderElement element, T dto);

    /**
     * Metoda wykonujaca walidację. Jest nadpisana w metodzie bazowj.
     * @param violations lista błędów do tej listy dodawane sa błedy zwiazane z konktretnym polem
     * @param orderElement pole które jest walidowane
     * @param orderFlowElementInStatus pole obrazujące element w danym statusie
     * @param orderFlowStatusTransitionElementFlag obiekt reprezentujacy flagi na przejscie
     * @param dto obiekt z nowymi danymi
     */
    void validate(List<EntityConstraintViolation> violations, OrderElement orderElement,
                  OrderFlowElementInStatus orderFlowElementInStatus, OrderFlowStatusTransitionElementFlag orderFlowStatusTransitionElementFlag, T dto);
}
