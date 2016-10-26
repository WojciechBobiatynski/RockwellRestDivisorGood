package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.enterprises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.dictionaries.ZipCodeDtoMapper;

/**
 * Maper mapujący EnterpriseDto na encję Enterprise
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class EnterpriseDtoMapper extends VersionableDtoMapper<EnterpriseDto,Enterprise>{

    @Autowired
    private ZipCodeDtoMapper zipCodeDtoMapper;

    @Autowired
    private EnterpriseContactDtoMapper enterpriseContactDtoMapper;
    @Override
    protected Enterprise initDestination() {
        return new Enterprise();
    }

    @Override
    protected void map(EnterpriseDto dto, Enterprise entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setAccountPayment(dto.getAccountPayment());
        entity.setAccountRepayment(dto.getAccountRepayment());
        entity.setName(dto.getName());
        entity.setVatRegNum(dto.getVatRegNum());
        entity.setAddressInvoice(dto.getAddressInvoice());
        entity.setZipCodeInvoice(dto.getZipCodeInvoice() == null ? null : zipCodeDtoMapper.convert(dto.getZipCodeInvoice()));
        entity.setAddressCorr(dto.getAddressCorr());
        entity.setZipCodeCorr(dto.getZipCodeInvoice() == null ? null: zipCodeDtoMapper.convert(dto.getZipCodeInvoice()));
        entity.setRemarks(dto.getRemarks());
        for (EnterpriseContactDto contact:dto.getContacts()) {
            entity.addContact(enterpriseContactDtoMapper.convert(contact));
        }
    }
}
