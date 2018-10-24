package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.googlecode.ehcache.annotations.Cacheable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.annotation.technical.asynch.ExtensionFeature;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.service.api.patterns.PatternContext;
import pl.sodexo.it.gryf.common.service.api.patterns.PatternService;
import pl.sodexo.it.gryf.common.utils.GrantProgramParamTypes;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;

import java.util.Date;
import java.util.Objects;

import static pl.sodexo.it.gryf.common.config.ApplicationParametersNames.GRYF_EXTERNAL_ORDER_ID_PATTERN;
import static pl.sodexo.it.gryf.common.config.ApplicationParametersNames.GRYF_IMPORT_TRAINING_SEARCH_PATT;

@Service(PatternService.IMPORT_TRAINING_PATTERN_SERVICE)
@Transactional(readOnly = true)
public class ImportTrainingPatternServiceImpl extends DefaultPatternService {

    private final static String CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID = "ImportTrainingPatternServiceImpl.getPattern";

    private final static String CACHE_PATTERN_SERVICE_GET_PATTERN_CODE = "ImportTrainingPatternServiceImpl.getPatternCode";

    @Autowired
    private ParamInDateService paramInDateService;

    @Override
    @ExtensionFeature(desc = "Zrobic konfiguracje per program - docelowo/umiescic w cache'u")
    @Cacheable(cacheName = CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID)
    public String getPattern(PatternContext<Long, String, String> patternContext) {

        Long grantProgramId = patternContext.getId();
        GrantProgramParam dbPatternGrantProgramPram = paramInDateService.findGrantProgramParam(grantProgramId, GrantProgramParamTypes.GRYF_IMPORT_TRAINING_SEARCH_PATT.name(), new Date(), false);

        if (Objects.nonNull(dbPatternGrantProgramPram)) {
            return dbPatternGrantProgramPram.getValue();
        }

        return findPatternUsingApplicationParameters(patternContext);

    }

    @Override
    @ExtensionFeature(desc = "Docelowo implementacja per program")
    @Cacheable(cacheName = CACHE_PATTERN_SERVICE_GET_PATTERN_CODE)
    public String getPatternParameterCode(PatternContext<Long, String, String> patternContext) {
        StringBuffer findingPatternByCode = new StringBuffer();
        if (Objects.nonNull(patternContext)) {
            findingPatternByCode.append(patternContext.getCode());
            findingPatternByCode.append(CONNECTOR);
        } else {
            findingPatternByCode.append(CONNECTOR);
        }
        findingPatternByCode.append(GRYF_IMPORT_TRAINING_SEARCH_PATT.name());

        return findingPatternByCode.toString();
    }

    @Override
    String getCode() {
        return GRYF_IMPORT_TRAINING_SEARCH_PATT.name();
    }
}
