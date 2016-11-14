package pl.sodexo.it.gryf.web.ti.response;

import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;

import java.util.List;

public class ValidationErrorResponse {

    //FIELDS FIELDS

    private final ResponseType responseType = ResponseType.VALIDATION_ERROR;

    private final List<EntityConstraintViolation> violations;

    //CONSTRUCTORS

    public ValidationErrorResponse(List<EntityConstraintViolation> violations) {
        this.violations = violations;
    }

    //GETTERS

    public ResponseType getResponseType() {
        return responseType;
    }

    public List<EntityConstraintViolation> getViolations() {
        return violations;
    }
    
}
