package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;

import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@Service
public class CareerDirectionRealizeOrderActionService extends ActionBaseService {

    public void execute(Order order, List<String> acceptedPathViolations){
        System.out.println("Realizuje zamówienie");
    }
}
