package pl.sodexo.it.gryf.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VatRegNumFormatConstraintValidator.class)
public @interface VatRegNumFormat {

    public String message() default "Błedny format NIP";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
