package pl.sodexo.it.gryf.service.validation.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransition;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.Objects;

/**
 * Walidator mozliwości wykonania akcji dla zamówienia w procesie
 *
 * Created by jbentyn on 2016-09-30.
 */
@Component
public class OrderActionValidator {

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private GryfValidator gryfValidator;

    public void validateVersion(Order order, Integer version) {
        if (!Objects.equals(order.getVersion(), version)) {
            throw new StaleDataException(order.getId(), order);
        }
    }

    /**
     * Sprawdza uprawnine do wykoninia akcji.
     *
     * @param statusTransition obiekt reprezentujacy przejscie pomiedzy akcjiami
     */
    public void validateActionPrivilege(OrderFlowStatusTransition statusTransition) {
        String privilege = statusTransition.getAugIdRequired();
        if (!StringUtils.isEmpty(privilege)) {
            if (!securityChecker.hasPrivilege(privilege)) {
                gryfValidator.validate("Nie posiadasz uprawnień do wykonania danej akcji");
            }
        }
    }

}
