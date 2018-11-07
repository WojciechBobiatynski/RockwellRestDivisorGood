package pl.sodexo.it.gryf.service.validation.publicbenefits.orders;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.service.api.patterns.DefaultPatternContext;
import pl.sodexo.it.gryf.service.api.patterns.PatternContext;
import pl.sodexo.it.gryf.service.api.patterns.PatternService;
import pl.sodexo.it.gryf.service.validation.publicbenefits.AbstractValidator;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adziobek on 07.11.2016.
 */
@Component
public class OrderValidator extends AbstractValidator {

    private static final String VIOLATIONS_PREFIX = "Dla zamówienia: ";


    @Autowired
    @Qualifier(PatternService.GRANT_PROGRAM_PATTERN_SERVICE)
    private PatternService grantProgramPatternService;


    public List<EntityConstraintViolation> validateContractIdAgreementWithPattern(GrantProgramDictionaryDTO grantProgram, String externalOrderId, String externalOrderIdPatternRegexp) {
        PatternContext importContractContext =  DefaultPatternContext.create().withCode(grantProgram.getProgramCode())
                .withId((Long) grantProgram.getId()).withDefaultPattern(externalOrderIdPatternRegexp).build();
        Pattern patternCompile = Pattern.compile(externalOrderIdPatternRegexp); //ToDo: grantProgramPatternService.getPattern(importContractContext));
        Matcher matcher = patternCompile.matcher(externalOrderId);
        if (!matcher.matches()) {
            List<EntityConstraintViolation> orderViolations = Lists.newArrayList();
            orderViolations.add(new EntityConstraintViolation("Identyfikator umowy musi być w formacie kod programu/numer/numer"));
            addPrefixMessage(VIOLATIONS_PREFIX, orderViolations);
            return orderViolations;
        }
        return Collections.EMPTY_LIST;
    }


}