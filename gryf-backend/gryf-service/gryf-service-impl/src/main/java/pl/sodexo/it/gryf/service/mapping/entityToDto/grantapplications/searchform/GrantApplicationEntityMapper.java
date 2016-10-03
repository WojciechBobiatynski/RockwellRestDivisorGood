package pl.sodexo.it.gryf.service.mapping.entityToDto.grantapplications.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.searchform.ZipCodeEntityToSearchResultMapper;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class GrantApplicationEntityMapper extends GryfEntityMapper<GrantApplication, GrantApplicationSearchResultDTO> {

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Autowired
    private ZipCodeEntityToSearchResultMapper zipCodeEntityToSearchResultMapper;

    @Override
    protected GrantApplicationSearchResultDTO initDestination() {
        return new GrantApplicationSearchResultDTO();
    }

    @Override
    public void map(GrantApplication entity, GrantApplicationSearchResultDTO dto) {
        super.map(entity, dto);
        GrantApplicationBasicData basicData = entity.getBasicData();
        dto.setId(entity.getId());
        dto.setStatus(dictionaryEntityMapper.convert(entity.getStatus()));
        dto.setEnterpriseId((entity.getEnterprise() != null) ? entity.getEnterprise().getId() : null);
        dto.setEnterpriseName(basicData != null ? basicData.getEnterpriseName() : null);
        dto.setVatRegNum(basicData != null ? basicData.getVatRegNum() : null);
        dto.setAddressInvoice(basicData != null ? basicData.getAddressInvoice() : null);
        dto.setZipCodeInvoice(zipCodeEntityToSearchResultMapper.convert(basicData != null ? basicData.getZipCodeInvoice() : null));
        dto.setApplyDate(entity.getApplyDate());
        dto.setConsiderationDate(entity.getConsiderationDate());
    }

}
