package pl.sodexo.it.gryf.common.validation.publicbenefits;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportContractDTO;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Komponent walidacji zewnetrzenego numeru zamówienia
 * przechowywanego w obiekcie klasy ImportContractDTO
 * w polu externalOrderId
 *
 */
@Component
public class ExternalOrderIdValidator implements ConstraintValidator<ValidExternalOrderId, ImportContractDTO> {

   /* @Autowired
    @Qualifier(PatternService.GRANT_PROGRAM_PATTERN_SERVICE)
    private PatternService grantProgramPatternService;
*/
    @PostConstruct
    public void init() {
        //intentionally empty
    }

    @Override
    public void initialize(ValidExternalOrderId validExternalOrderId) {
        //intentionally empty
    }

    @Override
    public boolean isValid(ImportContractDTO importContractDTO, ConstraintValidatorContext constraintValidatorContext) {
       /* PatternContext importContractContext =  DefaultPatternContext.create().withCode(importContractDTO.getGrantProgram().getProgramCode())
                .withId((Long) importContractDTO.getGrantProgram().getId()).build();
        Pattern patternCompile = Pattern.compile(grantProgramPatternService.getPattern(importContractContext));
        Matcher matcher = patternCompile.matcher(importContractDTO.getExternalOrderId());
        return matcher.matches();*/
        return true;
    }
}
