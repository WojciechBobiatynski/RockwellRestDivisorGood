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

@Service(PatternService.GRANT_PROGRAM_PATTERN_SERVICE)
@Transactional
public class ExternalOrderIdPatternServiceImpl extends DefaultPatternService {

    private final static String CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID = "ExternalOrderIdPatternServiceImpl.getPattern";

    private final static String CACHE_PATTERN_SERVICE_GET_PATTERN_CODE = "ExternalOrderIdPatternServiceImpl.getPatternCode";

    @Autowired
    private ParamInDateService paramInDateService;

    @Override
    @ExtensionFeature(desc = "Zrobic konfiguracje per program - docelowo/umiescic w cache'u")
    @Cacheable(cacheName = CACHE_PATTERN_SERVICE_GET_PATTERN_BY_ID)
    public String getPattern(PatternContext<Long, String, String> patternContext) {

        Long grantProgramId = patternContext.getId();
        GrantProgramParam dbPatternGrantProgramPram = paramInDateService.findGrantProgramParam(grantProgramId, GrantProgramParamTypes.GRYF_EXTERNAL_ORDER_ID_PATTERN.name(), new Date(), false);

        if (Objects.nonNull(dbPatternGrantProgramPram)) {
            return dbPatternGrantProgramPram.getValue();
        }

        return findPatternUsingApplicationParameters(patternContext);

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
        findingPatternByCode.append(getCode());

        return findingPatternByCode.toString();
    }

    @Override
    String getCode() {
        return GRYF_EXTERNAL_ORDER_ID_PATTERN.name();
    }

}
