package pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.TrainingInstitutionContactDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.TrainingInstitutionDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.VersionableDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.dictionaries.ZipCodeDtoMapper;

/**
 * Maper mapujący dto TrainingInstitutionDto na encję TrainingInstitution
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class TrainingInstitutionDtoMapper extends VersionableDtoMapper<TrainingInstitutionDto, TrainingInstitution> {

    @Autowired
    private ZipCodeDtoMapper zipCodeDtoMapper;

    @Autowired
    TrainingInstitutionContactDtoMapper trainingInstitutionContactDtoMapper;

    @Override
    protected TrainingInstitution initDestination() {
        return new TrainingInstitution();
    }

    @Override
    protected void map(TrainingInstitutionDto dto, TrainingInstitution entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
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
    }
}
