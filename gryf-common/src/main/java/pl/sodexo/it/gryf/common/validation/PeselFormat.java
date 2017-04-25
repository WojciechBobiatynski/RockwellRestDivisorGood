package pl.sodexo.it.gryf.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeselFormatConstraintValidator.class)
public @interface PeselFormat {

    public String message() default "Błędny format PESEL";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
