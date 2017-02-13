package pl.sodexo.it.gryf.service.mapping.dtotoentity.security.traininginstitutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.TiUserResetAttemptDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionUserRepository;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TiUserResetAttempt;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * * Komponent mapujący Dto użytkownika Usługodawcy na encję
 *
 * Created by akmiecinski on 24.10.2016.
 */
@Component
public class TiUserResetAttemptDtoMapper extends VersionableDtoMapper<TiUserResetAttemptDto, TiUserResetAttempt> {

    @Autowired
    private TrainingInstitutionUserRepository trainingInstitutionUserRepository;

    @Override
    protected TiUserResetAttempt initDestination() {
        return new TiUserResetAttempt();
    }

    @Override
    protected void map(TiUserResetAttemptDto dto, TiUserResetAttempt entity) {
        super.map(dto, entity);
        entity.setTurId(dto.getTurId());
        entity.setTrainingInstitutionUser(trainingInstitutionUserRepository.get(dto.getTiuId()));
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setUsed(dto.isUsed());
    }
}
