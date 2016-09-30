package pl.sodexo.it.gryf.service.validation.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupDeliverReimbursementDelivery;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupRegisterReimbursementDelivery;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupSecondaryReimbursementDelivery;

/**
 * Created by jbentyn on 2016-09-30.
 */
public enum ReimbursementDeliverySaveType {

    REGISTER(ValidationGroupRegisterReimbursementDelivery.class),
    DELIVER(ValidationGroupDeliverReimbursementDelivery.class),
    SECONDARY(ValidationGroupSecondaryReimbursementDelivery.class);

    private Class<?> validateClass;

    ReimbursementDeliverySaveType(Class<?> validateClass) {
        this.validateClass = validateClass;
    }

    public Class<?> getValidateClass() {
        return validateClass;
    }
}
