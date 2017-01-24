package pl.sodexo.it.gryf.service.mapping.entitytodto.security.traininginstitutions;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.TiUserResetAttemptDto;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TiUserResetAttempt;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;

/**
 * Komponent mapujący encje użytkownika Usługodawcy na DTO
 *
 * Created by akmiecinski on 24.10.2016.
 */
@Component
public class TiUserResetAttemptEntityMapper extends VersionableEntityMapper<TiUserResetAttempt, TiUserResetAttemptDto> {

    @Override
    protected TiUserResetAttemptDto initDestination() {
        return new TiUserResetAttemptDto();
    }

    @Override
    public void map(TiUserResetAttempt entity, TiUserResetAttemptDto dto){
        super.map(entity, dto);
        dto.setTurId(entity.getTurId());
        dto.setTiuId(entity.getTrainingInstitutionUser().getId());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setUsed(entity.isUsed());
    }

}
