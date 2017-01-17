package pl.sodexo.it.gryf.service.api.publicbenefits.orders;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;

import java.util.List;

/**
 * Serwis odpowiedzialny za zmianę stanu zamówienia w procesie
 *
 * Created by jbentyn on 2016-09-27.
 */
public interface OrderActionService {

    /**
     * Wykonanie akcji.
     *
     * @param id id zamowienia
     * @param actionId id kacji
     * @param incomingOrderElements elementy z gui
     * @param files pliki które maja zasotać dodane
     * @param acceptedViolations błędy zaakceptowane przez użytkonika
     */
    void executeAction(Long id, Long actionId, Integer version, List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files, List<String> acceptedViolations);

    void executeMainAction(Long id, Long actionId, Integer version, List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files, List<String> acceptedViolations);

    boolean executeAutomaticActions(Long orderId);

    boolean isActionAutomatic(Long orderId, String nextStatusId);

}
