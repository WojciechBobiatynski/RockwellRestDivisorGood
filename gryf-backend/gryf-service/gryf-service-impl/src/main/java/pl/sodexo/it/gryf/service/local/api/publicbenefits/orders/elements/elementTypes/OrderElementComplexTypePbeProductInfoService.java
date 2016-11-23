package pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypePbeProductInfoDTO;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.OrderElementService;

import java.math.BigDecimal;

/**
 * Created by Isolution on 2016-11-22.
 */
public interface OrderElementComplexTypePbeProductInfoService extends OrderElementService<OrderElementComplexTypePbeProductInfoDTO> {

    void addPbeProductElements(Order order, Contract contract, CreateOrderDTO dto);
}