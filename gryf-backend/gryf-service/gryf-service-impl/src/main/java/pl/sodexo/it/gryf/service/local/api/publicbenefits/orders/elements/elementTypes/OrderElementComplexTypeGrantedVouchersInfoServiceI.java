package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeGrantedVouchersInfoDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.OrderElementService;

/**
 * Created by jbentyn on 2016-10-06.
 */
public interface OrderElementComplexTypeGrantedVouchersInfoServiceI extends OrderElementService<OrderElementComplexTypeGrantedVouchersInfoDTO> {

    /**
     * Dodaje elementy do zamówienia które przetrzymuja informacje voucherach.
     * @param order zamówienie
     */
    void addVouchersInfoElements(Order order);
}
