package pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.RejectionDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.List;

/**
 * Created by kantczak on 2016-12-01.
 */
@Component
public class RejectionValidator {

    @Autowired
    private GryfValidator gryfValidator;

    public void validateRejection(RejectionDto rejectionDto) {
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(rejectionDto);
        gryfValidator.validate(violations);
    }

}
