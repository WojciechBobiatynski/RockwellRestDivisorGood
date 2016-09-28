package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders;

import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowAllowedDelay;

import java.util.Date;

/**
 * Serwis odpowiedzialny za pobieranie dat wymaganych dla zamówienia
 *
 * Created by jbentyn on 2016-09-28.
 */
public interface OrderDateService {

    /**
     * Metoda wyszukuje obiekt OrderFlowAllowedDelay na podstawie id przepływu zamówienia i typu dopuszczalnego opóźnienia. Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja obiektu OrderFlowAllowedDelay dla zadanych parametrów. .
     *
     * @param orderFlowId id przepływu zamówienia
     * @param delayTypeId id typu opóźnienia
     * @return objekt typu OrderFlowAllowedDelay
     */
    OrderFlowAllowedDelay findOrderFlowAllowedDelay(Long orderFlowId, String delayTypeId);

    /**
     * Metoda wyznacza datę wymaganą uzupełnienia elementu zamówienia na podstawie dopuszczalnego opóźnienia i obiektu zamówienia
     *
     * @param orderFlowAllowedDelay Dopuszczalny typ opóźnienia
     * @param order Zamówienie
     * @return wyznaczona data wymagana wypełnienia pola
     */
    Date calculateRequiredDate(OrderFlowAllowedDelay orderFlowAllowedDelay, Order order);

    /**
     * Aktualizuje daty wymagane uzupełnienia danych dla tych elementów zamówienia, dla których nie było takiej możliwości wcześniej
     *
     * @param order zamowienie
     */
    void fillRequiredDateForLazyFields(Order order);
}
