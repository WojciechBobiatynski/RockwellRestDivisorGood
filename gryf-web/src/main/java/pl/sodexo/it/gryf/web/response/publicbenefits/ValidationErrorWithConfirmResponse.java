package pl.sodexo.it.gryf.web.response.publicbenefits;

import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.web.response.ResponseType;

import java.util.List;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-29.
 */
public class ValidationErrorWithConfirmResponse {
    //FIELDS FIELDS

    private final ResponseType responseType = ResponseType.VALIDATION_WITH_CONFIRM_ERROR;

    private final List<EntityConstraintViolation> violations;

    //CONSTRUCTORS

    public ValidationErrorWithConfirmResponse(List<EntityConstraintViolation> violations) {
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
