package pl.sodexo.it.gryf.common.validation.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.annotation.technical.asynch.ExtensionFeature;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportContractDTO;
import pl.sodexo.it.gryf.common.service.api.programs.DefaultPatternContext;
import pl.sodexo.it.gryf.common.service.api.programs.PatternContext;
import pl.sodexo.it.gryf.common.service.api.programs.PatternService;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Komponent walidacji zewnetrzenego numeru zamówienia
 * przechowywanego w obiekcie klasy ImportContractDTO
 * w polu externalOrderId
 *
 */
@Component
public class ExternalOrderIdValidator implements ConstraintValidator<ValidExternalOrderId, ImportContractDTO> {

    @Autowired
    @Qualifier(PatternService.GRANT_PROGRAM_PATTERN_SERVICE)
    private PatternService grantProgramPatternService;

    @PostConstruct
    public void init() {
        //intentionally empty
    }

    @Override
    public void initialize(ValidExternalOrderId validExternalOrderId) {
        //intentionally empty
    }

    @Override
    @ExtensionFeature(desc = "Czy rozubować o powiazanie z kodem programu pobranym z zewnatrz? Create builder z opcjami with*")
    public boolean isValid(ImportContractDTO importContractDTO, ConstraintValidatorContext constraintValidatorContext) {
        PatternContext importContractContext = DefaultPatternContext.create().build();
        Pattern patternCompile = Pattern.compile(grantProgramPatternService.getPattern(importContractContext));
        Matcher matcher = patternCompile.matcher(importContractDTO.getExternalOrderId());
        return matcher.matches();
    }
}
