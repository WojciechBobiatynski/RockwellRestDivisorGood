package pl.sodexo.it.gryf.service.local.impl.publicbenefits.grantapplications.versions;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.versions.GrantApplicationV1DTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationRepData;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.grantapplications.GrantApplicationV0BaseService;

/**
 * Created by tomasz.bilski.ext on 2015-06-25.
 */
@Service
@Transactional
public class GrantApplicationV1Service extends GrantApplicationV0BaseService<GrantApplicationV1DTO> {

    @Override
    protected GrantApplicationRepData fillApplicationReportData(GrantApplicationRepData repData, GrantApplicationV1DTO dto) {
        repData.setVarchar001(dto.getRegon());
        return repData;
    }
}
