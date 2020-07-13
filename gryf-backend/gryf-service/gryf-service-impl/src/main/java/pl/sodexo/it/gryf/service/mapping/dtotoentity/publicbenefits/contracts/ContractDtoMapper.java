package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform.ContractDTO;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.dictionaries.ZipCodeDtoMapper;

/**
 * Maper mapujący ContractDto na encję Contract
 *
 * Created by adziobek on 31.10.2016.
 */
@Component
public class ContractDtoMapper extends VersionableDtoMapper<ContractDTO,Contract> {

    @Autowired
    private ZipCodeDtoMapper zipCodeDtoMapper;


    @Override
    protected Contract initDestination() {
        return new Contract();
    }

    protected void map(ContractDTO dto, Contract entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setSignDate(dto.getSignDate());
        entity.setExpiryDate(dto.getExpiryDate());

        entity.setCode(dto.getCode());
        entity.setAccountPayment(dto.getAccountPayment());

        entity.setAddressInvoice(dto.getAddressInvoice());
        entity.setZipCodeInvoice(dto.getZipCodeInvoice() == null ? null : zipCodeDtoMapper.convert(dto.getZipCodeInvoice()));
        entity.setAddressCorr(dto.getAddressCorr());
        entity.setZipCodeCorr(dto.getZipCodeInvoice() == null ? null: zipCodeDtoMapper.convert(dto.getZipCodeCorr()));
    }

}