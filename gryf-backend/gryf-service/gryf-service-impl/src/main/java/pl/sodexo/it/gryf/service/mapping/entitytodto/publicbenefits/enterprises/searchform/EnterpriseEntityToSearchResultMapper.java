package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.enterprises.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.service.mapping.GenericMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.searchform.ZipCodeEntityToSearchResultMapper;

/**
 * Maper mapujący encję EnterpriseContact na EnterpriseContactDto
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class EnterpriseEntityToSearchResultMapper extends GenericMapper<Enterprise,EnterpriseSearchResultDTO> {

    @Autowired
    private ZipCodeEntityToSearchResultMapper zipCodeEntityToSearchResultMapper;

    @Override
    protected EnterpriseSearchResultDTO initDestination() {
        return new EnterpriseSearchResultDTO();
    }

    @Override
    public void map(Enterprise entity, EnterpriseSearchResultDTO dto) {
        dto.setName(entity.getName());
        dto.setVatRegNum(entity.getVatRegNum());
        dto.setAddressInvoice(entity.getAddressInvoice());
        dto.setZipCodeInvoice(zipCodeEntityToSearchResultMapper.convert(entity.getZipCodeInvoice()));
        dto.setAddressCorr(entity.getAddressCorr());
        dto.setZipCodeCorr(zipCodeEntityToSearchResultMapper.convert(entity.getZipCodeCorr()));
        dto.setId(entity.getId());
    }
}
