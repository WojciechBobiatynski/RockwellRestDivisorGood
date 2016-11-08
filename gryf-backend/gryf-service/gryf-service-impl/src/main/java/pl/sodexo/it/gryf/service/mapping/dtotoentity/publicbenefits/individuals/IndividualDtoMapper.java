package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.model.publicbenefits.employment.Employment;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.dictionaries.ZipCodeDtoMapper;

import java.util.function.Consumer;

/**
 * Mapper mapujący dto IndividualDto na encję Individual
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class IndividualDtoMapper extends VersionableDtoMapper<IndividualDto, Individual> {

    @Autowired
    private ZipCodeDtoMapper zipCodeDtoMapper;

    @Autowired
    private IndividualContactDtoMapper individualContactDtoMapper;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Override
    protected Individual initDestination() {
        return new Individual();
    }

    @Override
    protected void map(IndividualDto dto, Individual entity) {
        super.map(dto, entity);

        Consumer<EnterpriseDto> myConsumer = enterpriseDto -> {
            Employment employment = new Employment();
            employment.setEnterprise(enterpriseRepository.get(enterpriseDto.getId()));
            entity.addEmployment(employment);
        };

        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setAccountPayment(dto.getAccountPayment());
        entity.setAccountRepayment(dto.getAccountRepayment());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPesel(dto.getPesel());
        entity.setSex(dto.getSex());
        entity.setDocumentType(dto.getDocumentType());
        entity.setDocumentNumber(dto.getDocumentNumber());
        entity.setAddressInvoice(dto.getAddressInvoice());
        entity.setZipCodeInvoice(dto.getZipCodeInvoice() == null ? null : zipCodeDtoMapper.convert(dto.getZipCodeInvoice()));
        entity.setAddressCorr(dto.getAddressCorr());
        entity.setZipCodeCorr(dto.getZipCodeCorr() == null ? null : zipCodeDtoMapper.convert(dto.getZipCodeCorr()));
        entity.setRemarks(dto.getRemarks());
        for (IndividualContactDto contactDto : dto.getContacts()) {
            entity.addContact(individualContactDtoMapper.convert(contactDto));
        }
        dto.getEnterprises().stream().forEach(myConsumer);
    }

}
