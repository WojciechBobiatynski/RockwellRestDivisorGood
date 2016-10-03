package pl.sodexo.it.gryf.common.exception.publicbenefits.grantapplications;

import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-29.
 */
public class EntityValidationWithConfirmException extends RuntimeException {

    //FIELDS

    private final List<EntityConstraintViolation> violations;

    //CONSTRUCTORS

    public EntityValidationWithConfirmException(List<EntityConstraintViolation> violations) {
        this.violations = new ArrayList<>(violations);
    }

    //GETTERS

    public List<EntityConstraintViolation> getViolations() {
        return violations;
    }

}
