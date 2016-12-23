package pl.sodexo.it.gryf.web.ind.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.web.ind.response.ValidationErrorResponse;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionHandlers {

    @ExceptionHandler(EntityValidationException.class)
    @ResponseBody
    public ResponseEntity<ValidationErrorResponse> validationException(EntityValidationException sde) {
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(sde.getViolations()));
    }

}
