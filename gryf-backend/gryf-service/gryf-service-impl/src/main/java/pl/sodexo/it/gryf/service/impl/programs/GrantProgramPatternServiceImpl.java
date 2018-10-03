package pl.sodexo.it.gryf.service.impl.programs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.annotation.technical.asynch.ExtensionFeature;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.config.ApplicationParametersNames;
import pl.sodexo.it.gryf.common.service.api.programs.PatternContext;
import pl.sodexo.it.gryf.common.service.api.programs.PatternService;

@Service (PatternService.GRANT_PROGRAM_PATTERN_SERVICE)
@Transactional
public class GrantProgramPatternServiceImpl implements PatternService<Long, String, String> {

    @Autowired
    private ApplicationParameters applicationParameters;

    @Override
    @ExtensionFeature(desc = "Zrobic konfiguracje per program - docelowo")
    public String getPattern(PatternContext<Long, String, String> patternContext) {
        return applicationParameters.getExternalOrderIdPatternRegexp();
    }

    @Override
    @ExtensionFeature(desc =  "Docelowo: Zbudowac mechanizm wyboru walidacji specyficzny")
    public String getPatternParameterCode(PatternContext<Long, String, String> pat) {
        return ApplicationParametersNames.GRYF_EXTERNAL_ORDER_ID_PATTERN.name();
    }
}
