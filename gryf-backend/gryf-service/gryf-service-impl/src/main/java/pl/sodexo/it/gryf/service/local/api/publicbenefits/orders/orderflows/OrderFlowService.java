package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows;

import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;

/**
 * Instancje dla danego interfeju są powoływane na podstawie objektów OrderFlow przetrzymywanych w bazie.
 * Interfejs jest odpowiedzialny za operacjie w ramach danych OrderFlow.
 * Różne OrderFlow mogą w inny sposób implementować dfany interfejs.
 * Created by tomasz.bilski.ext on 2015-08-19.
 */
public interface OrderFlowService {

    /**
     * Tworzy zamówienie dla danego wnioosku w danym order flow.
     * Nie dodaje elementów do zamówienia na podstawie inicjalnego statusu.
     * @param grantApplication wniosek aplikacji
     * @param orderFlow order flow
     * @return zamówienie
     */
    Order createOrder(GrantApplication grantApplication, OrderFlow orderFlow);
}
