package pl.sodexo.it.gryf.service.validation.asynchjobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.asynchjobs.detailsform.AsynchJobDetailsDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.List;

@Component
public class AsynchJobValidator {

    @Autowired
    private GryfValidator gryfValidator;

    public void validateJobProperties(AsynchJobDetailsDTO asynchJobDetailsDTO) {

        //GENERAL VALIDATION
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(asynchJobDetailsDTO);

        //VALIDATE (EXCEPTION)
        gryfValidator.validate(violations);
    }

}
