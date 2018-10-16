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

import java.util.Objects;

import static pl.sodexo.it.gryf.common.config.ApplicationParametersNames.GRYF_IMPORT_TRAINING_SEARCH_PATT;

@Service(PatternService.IMPORT_TRAINING_PATTERN_SERVICE)
@Transactional(readOnly = true)
public class ImportTrainingPatternServiceImpl implements PatternService<Long, String, String> {

    private final static String CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID = "ImportTrainingPatternServiceImpl.getPattern";

    private final static String CACHE_PATTERN_SERVICE_GET_PATTERN_CODE = "ImportTrainingPatternServiceImpl.getPatternCode";

    @Autowired
    private ApplicationParameters applicationParameters;

    @Override
    @ExtensionFeature(desc = "Docelowo implementacja per program")
    @Cacheable(cacheName = CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID)
    public String getPattern(PatternContext patternContext) {
        String findingPatternByCode = getPatternParameterCode(patternContext);

        String foundPattern = applicationParameters.findParameterValueByCode(findingPatternByCode);
        if (StringUtils.isEmpty(foundPattern)) {
            //wybierz domyslny jezeli brak w konfiguracji
            foundPattern = applicationParameters.getImportTraningSearchPattern();
        }

        return foundPattern;
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
}
