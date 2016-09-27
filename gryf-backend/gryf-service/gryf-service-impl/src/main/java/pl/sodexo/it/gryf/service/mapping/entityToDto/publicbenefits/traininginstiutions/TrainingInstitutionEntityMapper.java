package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.TrainingInstitutionDto;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.mapping.entityToDto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.ZipCodeEntityMapper;

/**
 * Mapper mapujący encję TrainingInstitution na dto TrainingInstitutionDto
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class TrainingInstitutionEntityMapper extends VersionableEntityMapper<TrainingInstitution, TrainingInstitutionDto> {

    @Autowired
    private ZipCodeEntityMapper zipCodeEntityMapper;

    @Autowired
    private TrainingInstitutionContactEntityMapper trainingInstitutionContactEntityMapper;

    @Override
    protected TrainingInstitutionDto initDestination() {
        return new TrainingInstitutionDto();
    }

    @Override
    public void map(TrainingInstitution entity, TrainingInstitutionDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setVatRegNum(entity.getVatRegNum());
        dto.setAddressInvoice(entity.getAddressInvoice());
        dto.setZipCodeInvoice(zipCodeEntityMapper.convert(entity.getZipCodeInvoice()));
        dto.setAddressCorr(entity.getAddressCorr());
        dto.setZipCodeCorr(zipCodeEntityMapper.convert(entity.getZipCodeCorr()));
        dto.setRemarks(entity.getRemarks());
        dto.setContacts(trainingInstitutionContactEntityMapper.convert(entity.getContacts()));
    }
}
