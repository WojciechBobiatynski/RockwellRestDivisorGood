package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;

import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@Service
public class CareerDirectionRealizeOrderActionService extends ActionBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CareerDirectionRealizeOrderActionService.class);

    @Autowired
    private PbeProductInstancePoolService productInstancePoolService;

    //PUBLIC METHODS

    public void execute(Order order, List<String> acceptedPathViolations) {
        LOGGER.debug("Realizacja zamówienia (utworzenie puli bonów, przypisanie instancji) dla zamówienia [{}]", order.getId());
        productInstancePoolService.createProductInstancePool(order.getId());
    }

}
