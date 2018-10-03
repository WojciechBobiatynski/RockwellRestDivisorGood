package pl.sodexo.it.gryf.common.validation.publicbenefits;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { ExternalOrderIdValidator.class })
public @interface ValidExternalOrderId {

    String message() default "{pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.validation.ValidExternalOrderId.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}