package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.annotation.technical.asynch.ExtensionFeature;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.config.ApplicationParametersNames;
import pl.sodexo.it.gryf.common.service.api.programs.PatternContext;
import pl.sodexo.it.gryf.common.service.api.programs.PatternService;

@Service (PatternService.IMPORT_TRAINING_PATTERN_SERVICE)
@Transactional(readOnly = true)
public class ImportTrainingPatternServiceImpl implements PatternService {

    @Autowired
    private ApplicationParameters applicationParameters;

    @Override
    @ExtensionFeature(desc = "Docelowo implementacja per program" )
    public String getPattern(PatternContext patternContext) {
        return applicationParameters.getImportTraningSearchPattern();
    }

    @Override
    @ExtensionFeature(desc = "Docelowo implementacja per program")
    public String getPatternParameterCode(PatternContext pat) {
        return ApplicationParametersNames.GRYF_IMPORT_TRAINING_SEARCH_PATTERN.name();
    }
}
