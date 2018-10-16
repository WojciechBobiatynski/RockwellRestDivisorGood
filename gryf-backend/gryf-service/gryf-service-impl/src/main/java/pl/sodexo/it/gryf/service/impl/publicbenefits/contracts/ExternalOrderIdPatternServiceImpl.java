package pl.sodexo.it.gryf.service.impl.publicbenefits.contracts;

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

import static pl.sodexo.it.gryf.common.config.ApplicationParametersNames.GRYF_EXTERNAL_ORDER_ID_PATTERN;

@Service(PatternService.GRANT_PROGRAM_PATTERN_SERVICE)
@Transactional
public class ExternalOrderIdPatternServiceImpl implements PatternService<Long, String, String> {

    private final static String CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID = "ExternalOrderIdPatternServiceImpl.getPattern";

    private final static String CACHE_PATTERN_SERVICE_GET_PATTERN_CODE = "ExternalOrderIdPatternServiceImpl.getPatternCode";

    @Autowired
    private ApplicationParameters applicationParameters;

    @Override
    @ExtensionFeature(desc = "Zrobic konfiguracje per program - docelowo/umiescic w cache'u")
    @Cacheable(cacheName = CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID)
    public String getPattern(PatternContext<Long, String, String> patternContext) {
        String findingPatternByCode = getPatternParameterCode(patternContext);

        String foundPattern = applicationParameters.findParameterValueByCode(findingPatternByCode);
        if (StringUtils.isEmpty(foundPattern)) {
            //wybierz domyslny jezeli brak w konfiguracji
            foundPattern = applicationParameters.getExternalOrderIdPatternRegexp();
        }

        return foundPattern;
    }

    @Override
    @ExtensionFeature(desc = "Docelowo: Zbudowac mechanizm wyboru walidacji specyficzny")
    @Cacheable(cacheName = CACHE_PATTERN_SERVICE_GET_PATTERN_CODE)
    public String getPatternParameterCode(PatternContext<Long, String, String> patternContext) {
        StringBuffer findingPatternByCode = new StringBuffer();
        if (Objects.nonNull(patternContext)) {
            findingPatternByCode.append(patternContext.getCode());
            findingPatternByCode.append(CONNECTOR);
        } else {
            findingPatternByCode.append(CONNECTOR);
        }
        findingPatternByCode.append(GRYF_EXTERNAL_ORDER_ID_PATTERN.name());

        return findingPatternByCode.toString();
    }
}
