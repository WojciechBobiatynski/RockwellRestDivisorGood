package pl.sodexo.it.gryf.common.exception;

import java.util.ArrayList;
import java.util.List;

public class EntityValidationException extends RuntimeException {

    //FIELDS

    private final List<EntityConstraintViolation> violations;

    //CONSTRUCTORS

    public EntityValidationException(List<EntityConstraintViolation> violations) {
        this.violations = new ArrayList<>(violations);
    }

    //GETTERS

    public List<EntityConstraintViolation> getViolations() {
        return violations;
    }

}
