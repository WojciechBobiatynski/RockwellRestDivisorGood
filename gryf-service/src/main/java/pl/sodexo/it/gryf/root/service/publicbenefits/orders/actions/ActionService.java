package pl.sodexo.it.gryf.root.service.publicbenefits.orders.actions;

import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-24.
 */
public interface ActionService {

    void execute(Order order, List<String> acceptedPathViolations);

}
