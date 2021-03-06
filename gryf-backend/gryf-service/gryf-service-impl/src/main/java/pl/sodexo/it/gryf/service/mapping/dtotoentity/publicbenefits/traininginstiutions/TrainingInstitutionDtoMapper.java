package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstitutionDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.dictionaries.ZipCodeDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.security.traininginstitutions.GryfTiUserDtoMapper;

/**
 * Maper mapujący dto TrainingInstitutionDto na encję TrainingInstitution
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class TrainingInstitutionDtoMapper extends VersionableDtoMapper<TrainingInstitutionDto, TrainingInstitution> {

    @Autowired
    private ZipCodeDtoMapper zipCodeDtoMapper;

    @Autowired
    private TrainingInstitutionContactDtoMapper trainingInstitutionContactDtoMapper;

    @Autowired
    private GryfTiUserDtoMapper gryfTiUserDtoMapper;

    @Override
    protected TrainingInstitution initDestination() {
        return new TrainingInstitution();
    }

    @Override
    protected void map(TrainingInstitutionDto dto, TrainingInstitution entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setExternalId(dto.getExternalId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setVatRegNum(dto.getVatRegNum());
        entity.setAddressInvoice(dto.getAddressInvoice());
        entity.setZipCodeInvoice(dto.getZipCodeInvoice() == null ? null : zipCodeDtoMapper.convert(dto.getZipCodeInvoice()));
        entity.setAddressCorr(dto.getAddressCorr());
        entity.setZipCodeCorr(dto.getZipCodeCorr() == null ? null : zipCodeDtoMapper.convert(dto.getZipCodeCorr()));
        entity.setRemarks(dto.getRemarks());
        for (TrainingInstitutionContactDto contactDto : dto.getContacts()) {
            entity.addContact(trainingInstitutionContactDtoMapper.convert(contactDto));
        }
        dto.getUsers().stream().forEach(gryfTiUserDto -> entity.getTrainingInstitutionUsers().add(gryfTiUserDtoMapper.convert(gryfTiUserDto)));

        entity.setVersion(dto.getVersion());
        entity.setCreatedUser(dto.getCreatedUser());
        entity.setCreatedTimestamp(dto.getCreatedTimestamp());
        entity.setModifiedUser(dto.getModifiedUser());
        entity.setModifiedTimestamp(dto.getModifiedTimestamp());
    }
}
