package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Serwis obsługujący elementy przepływu zamówień
 *
 * Created by jbentyn on 2016-09-28.
 */
public interface OrderFlowElementService {

    /**
     * Dodaje elementy do danego zamówienia. Elementy dodawane są na podstawie list elementów w danym statusie (w statusie
     * danego zamówienia).
     *
     * @param order zamowienie
     */
    void addElementsByOrderStatus(Order order);

    /**
     * Dodaje konkretny element składowy zamówienia typu Varchar.
     *
     * @param order zamówienie
     * @param elementId identyfikator elementu
     * @param valueVarchar vartość elementu typu varchar
     */
    void addElementVarcharValue(Order order, final String elementId, String valueVarchar);

    /**
     * Dodaje konkretny element składowy zamówienia typu Number.
     *
     * @param order zamówienie
     * @param elementId identyfikator elementu
     * @param valueNumber vartość elementu typu number
     */
    void addElementNumberValue(Order order, final String elementId, BigDecimal valueNumber);

    /**
     * Dodaje pusty element
     * @param order zamówienie
     * @param elementId identyfikator elementu
     */
    void addElementEmpty(Order order, final String elementId);

    /**
     * Metoda tworzy liste z obiektami OrderElementDTO. Na podstawie pola IncomingOrderElementDTO.elementTypeComponentName wiemy na jaką klase parsować
     * pole IncomingOrderElementDTO.data. Po sprarsowaniu dodatkow doczepiamy pliki jeżeli zostal plik dodany (ustawione pole fileIncluded).
     *
     * @param incomingOrderElements lista z typem componentu oraz danymi do parsowania
     * @param files pliki zalacznikow
     * @return stworzona lista obiektow typu OrderElementDTO
     */
    List<OrderElementDTO> createElementDtoList(List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files);

    /**
     * Waliduje elementy. Dla elementów wywołuje metode validacyjną w zależnosci o typu elementu.
     *
     * @param order zamowienie
     * @param elementDtoList lista obiektów dto
     */
    void validateElements(Order order, List<OrderElementDTO> elementDtoList);

    /**
     * Metoda aktualizuje elementy zamówienia na podstawie listy objektów dto. Poszczególne
     * elementy aktualizowane są przez konretne serwisy, które odpowiadaja typom elementów.
     *
     * @param order zamowenie
     * @param elementDtoList lista dto
     */
    void updateElements(Order order, List<OrderElementDTO> elementDtoList);
}
