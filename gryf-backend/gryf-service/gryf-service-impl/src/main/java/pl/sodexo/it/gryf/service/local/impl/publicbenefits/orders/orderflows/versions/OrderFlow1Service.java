package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.orderflows.versions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.OrderElementComplexTypeGrantedVouchersInfoService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.orderflows.OrderFlowBaseService;

/**
 * Created by tomasz.bilski.ext on 2015-08-19.
 */
@Service
public class OrderFlow1Service extends OrderFlowBaseService{

    //FIELDS

    @Autowired
    private OrderElementComplexTypeGrantedVouchersInfoService orderElementComplexTypeGrantedVouchersInfoService;

    //PUBLIC METHODS

    @Override
    public Order createOrder(GrantApplication grantApplication, OrderFlow orderFlow) {
        Order order = super.createOrder(grantApplication, orderFlow);
        orderElementComplexTypeGrantedVouchersInfoService.addVouchersInfoElements(order);
        return order;
    }

}
