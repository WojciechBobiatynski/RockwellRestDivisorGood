package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.enterprises.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.service.mapping.GenericMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.AuditableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.searchform.ZipCodeEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.enterprises.ContactTypeEntityMapper;

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
