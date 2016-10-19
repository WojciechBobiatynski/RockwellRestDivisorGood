package pl.sodexo.it.gryf.service.validation.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowStatusTransition;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

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

    /**
     * Sprawdza uprawnine do wykoninia akcji.
     *
     * @param statusTransition obiekt reprezentujacy przejscie pomiedzy akcjiami
     */
    public void validateActionPrivilege(OrderFlowStatusTransition statusTransition) {
        String privilege = statusTransition.getAugIdRequired();
        if (!GryfStringUtils.isEmpty(privilege)) {
            if (!securityChecker.hasPrivilege(privilege)) {
                gryfValidator.validate("Nie posiadasz uprawnień do wykonania danej akcji");
            }
        }
    }

}
