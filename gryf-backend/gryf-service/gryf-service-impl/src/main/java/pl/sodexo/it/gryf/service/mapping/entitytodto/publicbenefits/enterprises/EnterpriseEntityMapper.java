package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.enterprises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.ZipCodeEntityMapper;

/**
 * Maper mapujący encję Enterprise na EnterpriseDto
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class EnterpriseEntityMapper extends VersionableEntityMapper<Enterprise, EnterpriseDto> {

    @Autowired
    private ZipCodeEntityMapper zipCodeEntityMapper;

    @Autowired
    private EnterpriseContactEntityMapper enterpriseContactEntityMapper;

    @Override
    protected EnterpriseDto initDestination() {
        return new EnterpriseDto();
    }

    @Override
    public void map(Enterprise entity, EnterpriseDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setAccountRepayment(entity.getAccountRepayment());
        dto.setName(entity.getName());
        dto.setVatRegNum(entity.getVatRegNum());
        dto.setAddressInvoice(entity.getAddressInvoice());
        dto.setZipCodeInvoice(zipCodeEntityMapper.convert(entity.getZipCodeInvoice()));
        dto.setAddressCorr(entity.getAddressCorr());
        dto.setZipCodeCorr(zipCodeEntityMapper.convert(entity.getZipCodeCorr()));
        dto.setRemarks(entity.getRemarks());
        dto.setContacts(enterpriseContactEntityMapper.convert(entity.getContacts()));

    }
}
