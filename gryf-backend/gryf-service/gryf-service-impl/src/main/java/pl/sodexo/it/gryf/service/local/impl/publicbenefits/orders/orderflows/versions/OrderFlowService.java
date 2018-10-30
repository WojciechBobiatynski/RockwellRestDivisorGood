package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.orderflows.versions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.OrderElementComplexTypePbeProductInfoService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.orderflows.OrderFlowBaseService;

/**
 * Created by Isolution on 2016-11-17.
 */
@Service (OrderFlowService.DEFAULT_ORDER_FLOW_SERVICE)
public class OrderFlowService extends OrderFlowBaseService {

    //FIELDS

    @Autowired
    private OrderElementComplexTypePbeProductInfoService orderElementComplexTypePbeProductInfoService;

    //PUBLIC METHODS

    @Override
    public Order createOrder(Contract contract, OrderFlow orderFlow, CreateOrderDTO dto) {
        Order order = super.createOrder(contract, orderFlow, dto);
        orderElementComplexTypePbeProductInfoService.addPbeProductElements(order, contract, dto);
        return order;
    }
}
