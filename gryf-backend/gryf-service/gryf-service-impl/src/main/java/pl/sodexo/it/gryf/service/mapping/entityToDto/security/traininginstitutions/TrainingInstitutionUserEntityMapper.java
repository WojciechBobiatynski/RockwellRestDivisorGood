package pl.sodexo.it.gryf.service.mapping.entityToDto.security.traininginstitutions;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;
import pl.sodexo.it.gryf.service.mapping.entityToDto.VersionableEntityMapper;

/**
 * Komponent mapujący encje użytkownika instytucji szkoleniowej na DTO
 *
 * Created by akmiecinski on 24.10.2016.
 */
@Component
public class TrainingInstitutionUserEntityMapper extends VersionableEntityMapper<TrainingInstitutionUser, GryfTiUserDto> {

    @Override
    protected GryfTiUserDto initDestination() {
        return new GryfTiUserDto();
    }

    @Override
    public void map(TrainingInstitutionUser entity, GryfTiUserDto dto){
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setTrainingInstitutionId(entity.getTrainingInstitution().getId());
        dto.setLogin(entity.getLogin());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setLastLoginSuccessDate(entity.getLastLoginSuccessDate());
        dto.setPasswordExpirationDate(entity.getPasswordExpirationDate());
        dto.setActive(entity.getIsActive());
        dto.setLastLoginFailureDate(entity.getLastLoginFailureDate());
        dto.setLoginFailureAttempts(entity.getLoginFailureAttempts());
    }

}
