package pl.sodexo.it.gryf.web.ti.advice;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.exception.verification.GryfIndUserVerificationException;
import pl.sodexo.it.gryf.web.ti.response.GeneralExceptionResponse;
import pl.sodexo.it.gryf.web.ti.response.IndUserVerificationExceptionResponse;
import pl.sodexo.it.gryf.web.ti.response.ValidationErrorResponse;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionHandlers {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlers.class);

    @Autowired
    ApplicationParameters aps;

    @ExceptionHandler(EntityValidationException.class)
    @ResponseBody
    public ResponseEntity<ValidationErrorResponse> validationException(EntityValidationException sde) {
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(sde.getViolations()));
    }

    @ExceptionHandler(GryfIndUserVerificationException.class)
    @ResponseBody
    public ResponseEntity<IndUserVerificationExceptionResponse> validationException(GryfIndUserVerificationException sde) {
        return ResponseEntity.badRequest().body(new IndUserVerificationExceptionResponse(sde.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<GeneralExceptionResponse> generalException(Exception sde) {
        LOGGER.error(sde.getMessage(), sde);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GeneralExceptionResponse(sde.getMessage(), Throwables.getStackTraceAsString(sde)));
    }
    
}