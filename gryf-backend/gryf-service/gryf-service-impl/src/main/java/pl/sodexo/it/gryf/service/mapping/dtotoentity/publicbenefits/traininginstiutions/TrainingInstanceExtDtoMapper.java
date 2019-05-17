package pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.traininginstiutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.detailsform.TrainingInstanceExtDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.asynch.AsynchronizeJobRepository;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstanceExt;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

@Component
public class TrainingInstanceExtDtoMapper extends VersionableDtoMapper<TrainingInstanceExtDTO, TrainingInstanceExt> {

    @Autowired
    private AsynchronizeJobRepository asynchronizeJobRepository;

    @Override
    protected TrainingInstanceExt initDestination() {
        return new TrainingInstanceExt();
    }

    @Override
    protected void map(TrainingInstanceExtDTO dto, TrainingInstanceExt entity) {
        super.map(dto, entity);

        if (dto.getTrainingInstitution() != null) {
            entity.setTrainingInstitution(new TrainingInstitution());
            entity.getTrainingInstitution().setId(dto.getTrainingInstitution());
        }

        if (dto.getTrainingId() != null) {
            entity.setTraining(new Training());
            entity.getTraining().setId(dto.getTrainingId());
        }

        entity.setVatRegNum(dto.getVatRegNum());
        entity.setName(dto.getName());
        entity.setTrainingExternalId(dto.getTrainingExternalId());
        entity.setTrainingName(dto.getTrainingName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setPlace(dto.getPlace());
        entity.setPrice(dto.getPrice());
        entity.setHoursNumber(dto.getHoursNumber());
        entity.setHourPrice(dto.getHourPrice());
        if (dto.getCategory() != null) {
            entity.setCategory(new TrainingCategory());
            entity.getCategory().setId(dto.getCategory());
        }
        entity.setSubcategory(dto.getSubcategory());
        entity.setIsExam(dto.getIsExam());
        entity.setCertificate(dto.getCertificate());
        entity.setCertificateRemark(dto.getCertificateRemark());
        entity.setIndOrderExternalId(dto.getIndOrderExternalId());
        entity.setStatus(dto.getStatus());
        entity.setIsQualification(dto.getIsQualification());
        entity.setIsOtherQualification(dto.getIsOtherQualification());
        entity.setQualificationCode(dto.getQualificationCode());
        entity.setRegistrationDate(dto.getRegistrationDate());
        entity.setMaxParticipantsCount(dto.getMaxParticipantsCount());
        entity.setPriceValidateType(dto.getPriceValidateType());
    }
}
