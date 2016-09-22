package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.common.VerifyOrderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Service
public class Verify1ActionService extends ActionBaseService {

    @Override
    protected void executeAction(Order order) {
    }

    @Override
    protected List<EntityConstraintViolation> generateConfirmViolations(Order order) {
        List<EntityConstraintViolation> violations = new ArrayList<>();
        VerifyOrderHelper.validateVouchersNumber(violations, order);
        return violations;
   }

    @Override
    protected List<EntityConstraintViolation> generateViolateViolations(Order order) {
        List<EntityConstraintViolation> violations = new ArrayList<>();
        VerifyOrderHelper.validateConfirmationCheckboxes(violations, order);
        return violations;
    }
    
}
