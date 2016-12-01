package pl.sodexo.it.gryf.service.validation.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.List;

/**
 * Created by kantczak on 2016-12-01.
 */
@Component
public class CorrectionValidator {

    @Autowired
    private GryfValidator gryfValidator;

    public void validateCorrection(CorrectionDto correctionDto) {
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(correctionDto);
        gryfValidator.validate(violations);
    }

}
