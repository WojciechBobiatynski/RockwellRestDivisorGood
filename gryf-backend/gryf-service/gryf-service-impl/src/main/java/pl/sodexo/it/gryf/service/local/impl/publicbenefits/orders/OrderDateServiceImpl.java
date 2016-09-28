package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowAllowedDelayRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowElementInStatusRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.*;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderDateService;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-28.
 */
@Service
@Transactional
public class OrderDateServiceImpl implements OrderDateService {

    @Autowired
    private OrderFlowAllowedDelayRepository orderFlowAllowedDelayRepository;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    @Autowired
    private OrderFlowElementInStatusRepository orderFlowElementInStatusRepository;

    /**
     * Metoda wyszukuje obiekt OrderFlowAllowedDelay na podstawie id przepływu zamówienia i typu dopuszczalnego opóźnienia. Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja obiektu OrderFlowAllowedDelay dla zadanych parametrów. .
     *
     * @param orderFlowId id przepływu zamówienia
     * @param delayTypeId id typu opóźnienia
     * @return objekt typu OrderFlowAllowedDelay
     */
    public OrderFlowAllowedDelay findOrderFlowAllowedDelay(Long orderFlowId, String delayTypeId) {
        List<OrderFlowAllowedDelay> orderFlowAllowedDelays = orderFlowAllowedDelayRepository.findByOrderFlowAndDelayType(orderFlowId, delayTypeId);
        if (orderFlowAllowedDelays.size() == 0) {
            throw new RuntimeException(
                    String.format("Błąd parmetryzacji w tabeli APP_PBE.ORDER_FLOW_ALLOWED_DELAYS. Nie znaleziono żadnego dopuszczalnego opóźnienia typu [%s] dla order flow dla wersji [%s]",
                            delayTypeId, orderFlowId));
        }
        if (orderFlowAllowedDelays.size() > 1) {
            throw new RuntimeException(
                    String.format("Błąd parmetryzacji w tabeli APP_PBE.ORDER_FLOW_ALLOWED_DELAYS. Dla danej wersji order flow [%s] znaleziono więcej niż jedno opuźnienie typu [%s]", orderFlowId,
                            delayTypeId));
        }
        return orderFlowAllowedDelays.get(0);
    }

    /**
     * Metoda wyznacza datę wymaganą uzupełnienia elementu zamówienia na podstawie dopuszczalnego opóźnienia i obiektu zamówienia
     *
     * @param orderFlowAllowedDelay Dopuszczalny typ opóźnienia
     * @param order Zamówienie
     * @return wyznaczona data wymagana wypełnienia pola
     */
    public Date calculateRequiredDate(OrderFlowAllowedDelay orderFlowAllowedDelay, Order order) {
        //determine delay start date
        Date delayStartDate = null;
        switch (orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId()) {
            case SYSDATE:
                delayStartDate = new Date();
                break;
            case ORDERDATE:
                delayStartDate = order.getOrderDate();
                break;
            case SIGNDATE:
                throw new UnsupportedOperationException("Typ daty początkowej opóźnienia " + orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId().getLabel() + " nieobsługiwany");
            case CONTRDATE:
                throw new UnsupportedOperationException("Typ daty początkowej opóźnienia " + orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId().getLabel() + " nieobsługiwany");
            case EXEDATE:
                throw new UnsupportedOperationException("Typ daty początkowej opóźnienia " + orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId().getLabel() + " nieobsługiwany");
            default:
                throw new UnsupportedOperationException("Typ daty początkowej opóźnienia " + orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId().getLabel() + " nieobsługiwany");
        }
        return gryfPLSQLRepository.getNthDay(delayStartDate, orderFlowAllowedDelay.getDelayValue(), orderFlowAllowedDelay.getDelayDaysType());
    }

    /**
     * Aktualizuje daty wymagane uzupełnienia danych dla tych elementów zamówienia, dla których nie było takiej możliwości wcześniej
     *
     * @param order zamowienie
     */
    public void fillRequiredDateForLazyFields(Order order) {
        for (final OrderElement oe : order.getOrderElements()) {
            // instancja elementu na zamówieniu nie ma uzupełnionej daty wymaganej
            if (oe.getRequiredDate() == null) {
                OrderFlowElementInStatus orderFlowElementInStatus = findOrderFlowElementInStatus(order.getStatus().getStatusId(), oe.getOrderFlowElement().getElementId());
                //element jest widoczny w aktualnym statusie zamówienia
                if (orderFlowElementInStatus != null) {
                    OrderFlowAllowedDelayType orderFlowAllowedDelayType = orderFlowElementInStatus.getOrderFlowAllowedDelayType();
                    // element w danym statusie ma w definicji datę wymaganą
                    if (orderFlowAllowedDelayType != null) {
                        OrderFlowAllowedDelay orderFlowAllowedDelayForElement = findOrderFlowAllowedDelay(order.getOrderFlow().getId(), orderFlowAllowedDelayType.getId());
                        oe.setRequiredDate(calculateRequiredDate(orderFlowAllowedDelayForElement, order));
                    }
                }
            }
        }
    }

    /**
     * Metoda wyszukuje obiekt OrderFlowElementInStatus na podstawie id statusu i id elementu. Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja obiektu OrderFlowElementInStatus dla zadanych parametrów.
     *
     * @param orderStatusId id statusu zamówienia
     * @param elementId id elementu
     * @return objekt typu OrderFlowElementInStatus
     */
    private OrderFlowElementInStatus findOrderFlowElementInStatus(String orderStatusId, String elementId) {
        List<OrderFlowElementInStatus> orderFlowElementsInStatus = orderFlowElementInStatusRepository.findByOrderStatusIdAndElementId(orderStatusId, elementId);
        if (orderFlowElementsInStatus.size() > 1) {
            throw new RuntimeException(
                    String.format("Błąd parmetryzacji w tabeli APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS. Znaleziono więcej niż jeden element [%s] dla statusu [%s]", elementId, orderStatusId));
        }
        if (orderFlowElementsInStatus.size() == 1) {
            return orderFlowElementsInStatus.get(0);
        }
        return null;
    }
}
