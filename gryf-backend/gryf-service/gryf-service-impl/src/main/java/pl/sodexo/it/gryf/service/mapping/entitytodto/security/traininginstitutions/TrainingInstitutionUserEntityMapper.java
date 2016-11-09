package pl.sodexo.it.gryf.service.mapping.entitytodto.security.traininginstitutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.security.RoleEntityMapper;

/**
 * Komponent mapujący encje użytkownika instytucji szkoleniowej na DTO
 *
 * Created by akmiecinski on 24.10.2016.
 */
@Component
public class TrainingInstitutionUserEntityMapper extends VersionableEntityMapper<TrainingInstitutionUser, GryfTiUserDto> {

    @Autowired
    private RoleEntityMapper roleEntityMapper;

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
        if(entity.getRoles() != null && !entity.getRoles().isEmpty()){
            dto.setRoles(roleEntityMapper.convert(entity.getRoles()));
        }
    }

}
