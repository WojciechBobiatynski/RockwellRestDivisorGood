package pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.actions.actionTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.actions.ActionBaseService;
import pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders.actions.common.VerifyOrderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Service
public class CheckVerify1ActionService extends ActionBaseService {

    @Override
    protected List<EntityConstraintViolation> generateViolateViolations(Order order) {
        List<EntityConstraintViolation> violations = new ArrayList<>();
        VerifyOrderHelper.validateConfirmationCheckboxes(violations, order);
        VerifyOrderHelper.validateVouchersNumber(violations, order);
        return violations;
    }
    
}