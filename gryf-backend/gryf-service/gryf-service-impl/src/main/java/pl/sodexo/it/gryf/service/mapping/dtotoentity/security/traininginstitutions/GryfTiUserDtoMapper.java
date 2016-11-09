package pl.sodexo.it.gryf.service.mapping.dtotoentity.security.traininginstitutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.security.RoleDtoMapper;

/**
 * Komponent mapujący Dto użytkownika instytucji szkoleniowej na encję
 *
 * Created by akmiecinski on 24.10.2016.
 */
@Component
public class GryfTiUserDtoMapper extends VersionableDtoMapper<GryfTiUserDto, TrainingInstitutionUser> {

    @Autowired
    private RoleDtoMapper roleDtoMapper;

    @Autowired
    private TrainingInstitutionRepository trainingInstitutionRepository;

    @Override
    protected TrainingInstitutionUser initDestination() {
        return new TrainingInstitutionUser();
    }

    @Override
    protected void map(GryfTiUserDto dto, TrainingInstitutionUser entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setTrainingInstitution(dto.getTrainingInstitutionId() == null ? null : trainingInstitutionRepository.get(dto.getTrainingInstitutionId()));
        entity.setLogin(dto.getLogin());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setLastLoginSuccessDate(dto.getLastLoginSuccessDate());
        entity.setPasswordExpirationDate(dto.getPasswordExpirationDate());
        entity.setIsActive(dto.isActive());
        entity.setLastLoginFailureDate(dto.getLastLoginFailureDate());
        entity.setLoginFailureAttempts(dto.getLoginFailureAttempts());
        if(dto.getRoles() != null && !dto.getRoles().isEmpty()){
            entity.setRoles(roleDtoMapper.convert(dto.getRoles()));
        }
    }
}
