package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Serwis lokalny dla zamówień
 *
 * Created by jbentyn on 2016-09-27.
 */
public interface OrderServiceLocal {

    /**
     * Metoda wypelnia obiekt dto potrzebny do tworzenia zamowiernia na podstawie dto
     * @param contract umowa
     * @return dto do tworzenia zamowienia
     */
    CreateOrderDTO createCreateOrderDTO(Contract contract);

    /**
     * Metoda tworzy i zapisuje do bazy obiekt zamowienia na podstawie dto.
     * @param dto obiekt zawierajace niezbedne informacjie do zapisania zamowienia
     * @return zamówienie
     */
    Order createOrder(CreateOrderDTO dto);

    /**
     * Metoda tworzy i zapisuje do bazy obiekt zamowienia na podstawie wniosku.
     * @param grantApplication wniosek
     * @return zamowienie
     */
    Order createOrder(GrantApplication grantApplication);

    /**
     * tworzy liste obiektów OrderElementDTO na podstawie obiektów OrderElementDTOBuilder
     * @param orderElementDTOBuilders lista obiektów buildera
     * @return lista obiektów OrderElementDTO
     */
    List<OrderElementDTO> createOrderElementDtolist(List<OrderElementDTOBuilder> orderElementDTOBuilders);

    /**
     * Metoda zwraca kwotę do wpłaty przez przedsiębiorstwo (MŚP) za przyznane bony szkoniowe
     * @param order zamówienie, dla którego kwota ma zostać wyznaczona
     * @return
     */
    BigDecimal getPaymentAmount(Order order);

}
