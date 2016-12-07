package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.orderflows.versions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.OrderElementComplexTypePbeProductInfoService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.orderflows.OrderFlowBaseService;

import java.util.Date;

import static pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementCons.*;
/**
 * Created by Isolution on 2016-11-17.
 */
@Service
public class OrderFlow100Service extends OrderFlowBaseService {

    //FIELDS

    @Autowired
    private OrderElementComplexTypePbeProductInfoService orderElementComplexTypePbeProductInfoService;

    @Autowired
    private OrderFlowElementService orderFlowElementService;

    //PUBLIC METHODS

    @Override
    public Order createOrder(Contract contract, OrderFlow orderFlow, CreateOrderDTO dto) {
        Order order = super.createOrder(contract, orderFlow, dto);
        orderElementComplexTypePbeProductInfoService.addPbeProductElements(order, contract, dto);
        orderFlowElementService.addElementVarcharValue(order, KK_ADDRESS_INVOICE_ELEM_ID, dto.getAddressInvoice());
        orderFlowElementService.addElementVarcharValue(order, KK_ADDRESS_CORRESPONDENCE_ELEM_ID, dto.getAddressCorr());
        orderFlowElementService.addElementVarcharValue(order, KK_POSTAL_CODE_INVOICE_ELEM_ID, dto.getZipCodeInvoice());
        orderFlowElementService.addElementVarcharValue(order, KK_POSTAL_CODE_CORRESPONDENCE_ELEM_ID, dto.getZipCodeCorr());
        return order;
    }
}
