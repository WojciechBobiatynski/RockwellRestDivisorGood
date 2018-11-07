package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.googlecode.ehcache.annotations.Cacheable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.service.api.patterns.PatternContext;
import pl.sodexo.it.gryf.service.api.patterns.PatternService;
import pl.sodexo.it.gryf.common.utils.GrantProgramParamTypes;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;

import java.util.Date;
import java.util.Objects;

import static pl.sodexo.it.gryf.common.config.ApplicationParametersNames.GRYF_IMPORT_TRAINING_SEARCH_PATT;

@Service(PatternService.IMPORT_TRAINING_PATTERN_SERVICE)
@Transactional
public class ImportTrainingPatternServiceImpl implements  PatternService<Long, String, String> {

    private final static String CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID = "DefaultPatternService.getPattern";

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private ParamInDateService paramInDateService;

    @Override
    @Cacheable(cacheName = CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID)
    @Transactional
    public String getPattern(PatternContext<Long, String, String> patternContext) {

        Long grantProgramId = patternContext.getId();
        GrantProgramParam dbPatternGrantProgramPram = paramInDateService.findGrantProgramParam(grantProgramId, getPatternCodeName(), new Date(), false);

        if (Objects.nonNull(dbPatternGrantProgramPram)) {
            return dbPatternGrantProgramPram.getValue();
        }

        return findPatternUsingApplicationParameters(patternContext);

    }

    private String findPatternUsingApplicationParameters(PatternContext<Long, String, String> patternContext) {
        String findingPatternByCode = getPatternParameterCode(patternContext);

        String foundPattern = applicationParameters.findParameterValueByCode(findingPatternByCode);
        if (StringUtils.isEmpty(foundPattern)) {
            //wybierz domyslny jezeli brak w konfiguracji
            foundPattern = applicationParameters.getImportTraningSearchPattern();
        }

        return foundPattern;
    }

    private String getPatternParameterCode(PatternContext<Long, String, String> patternContext) {
        StringBuffer findingPatternByCode = new StringBuffer();
        if (Objects.nonNull(patternContext)) {
            findingPatternByCode.append(patternContext.getCode());
            findingPatternByCode.append(CONNECTOR);
        } else {
            findingPatternByCode.append(CONNECTOR);
        }
        findingPatternByCode.append(getPatternCodeName());

        return findingPatternByCode.toString();
    }

    @Override
    public String getCode() {
        return GRYF_IMPORT_TRAINING_SEARCH_PATT.name();
    }

    @Override
    public String getPatternCodeName(){
        return GrantProgramParamTypes.GRYF_IMPORT_TRAINING_SEARCH_PATT.name();
    }

}
