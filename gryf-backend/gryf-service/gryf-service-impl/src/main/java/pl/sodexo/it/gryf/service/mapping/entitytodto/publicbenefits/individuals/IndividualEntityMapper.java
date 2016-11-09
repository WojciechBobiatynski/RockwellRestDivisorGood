package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.model.publicbenefits.employment.Employment;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.ZipCodeEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.enterprises.EnterpriseEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.security.RoleEntityMapper;

/**
 * Maper mapujący encję Individual na dto  IndividualDto
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class IndividualEntityMapper extends VersionableEntityMapper<Individual, IndividualDto> {

    @Autowired
    private ZipCodeEntityMapper zipCodeEntityMapper;

    @Autowired
    private IndividualContactEntityMapper individualContactEntityMapper;

    @Autowired
    private EnterpriseEntityMapper enterpriseEntityMapper;

    @Autowired
    private RoleEntityMapper roleEntityMapper;

    @Override
    protected IndividualDto initDestination() {
        return new IndividualDto();
    }

    @Override
    public void map(Individual entity, IndividualDto dto) {
        super.map(entity, dto);

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setAccountPayment(entity.getAccountPayment());
        dto.setAccountRepayment(entity.getAccountRepayment());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPesel(entity.getPesel());
        dto.setSex(entity.getSex());
        dto.setDocumentType(entity.getDocumentType());
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setAddressInvoice(entity.getAddressInvoice());
        dto.setZipCodeInvoice(zipCodeEntityMapper.convert(entity.getZipCodeInvoice()));
        dto.setAddressCorr(entity.getAddressCorr());
        dto.setZipCodeCorr(zipCodeEntityMapper.convert(entity.getZipCodeCorr()));
        dto.setRemarks(entity.getRemarks());
        dto.setContacts(individualContactEntityMapper.convert(entity.getContacts()));
        if(entity.getIndividualUser() != null){
            dto.setVerificationCode(AEScryptographer.decrypt(entity.getIndividualUser().getVerificationCode()));
            dto.setLastLoginDate(entity.getIndividualUser().getLastLoginSuccessDate());

            if(entity.getIndividualUser().getRoles() != null){
                dto.setRoles(roleEntityMapper.convert(entity.getIndividualUser().getRoles()));
            }
        }

        //kolejny przypadek gdzie nie działa stream, dlatego tak
        for(Employment employment : entity.getEmployments()){
            dto.getEnterprises().add(enterpriseEntityMapper.convert(employment.getEnterprise()));
        }
    }
}
