package pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements;

import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementInStatus;

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
     * @param dto obiekt z nowymi danymi
     */
    void validate(List<EntityConstraintViolation> violations, OrderElement orderElement, OrderFlowElementInStatus orderFlowElementInStatus, T dto);
}
