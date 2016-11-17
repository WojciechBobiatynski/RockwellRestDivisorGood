package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders;

import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import java.math.BigDecimal;

/**
 * Serwis lokalny dla zamówień
 *
 * Created by jbentyn on 2016-09-27.
 */
public interface OrderServiceLocal {

    /**
     * Metoda tworzy i zapisuje do bazy obiekt zamowienia na podstawie wniosku.
     *
     * @param grantApplication wniosek
     * @return zamowienie
     */
    Order createOrder(GrantApplication grantApplication);

    /**
     * Metoda tworzy i zapisuje do bazy obiekt zamowienia na podstawie umowy.
     *
     * @param contract umowa
     * @return zamowienie
     */
    Order createOrder(Contract contract);

    /**
     * Metoda zwraca kwotę do wpłaty przez przedsiębiorstwo (MŚP) za przyznane bony szkoniowe
     *
     * @param order zamówienie, dla którego kwota ma zostać wyznaczona
     * @return
     */
    BigDecimal getPaymentAmount(Order order);

}
