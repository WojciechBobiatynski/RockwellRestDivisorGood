package pl.sodexo.it.gryf.service.mapping.dtotoentity.security.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * Komponent mapujący Dto na encję
 *
 * Created by akmiecinski on 21.10.2016.
 */
@Component
public class GryfIndUserDtoMapper extends VersionableDtoMapper<GryfIndUserDto, IndividualUser> {

    @Autowired
    private IndividualRepository individualRepository;

    @Override
    protected IndividualUser initDestination() {
        return new IndividualUser();
    }

    @Override
    protected void map(GryfIndUserDto dto, IndividualUser entity) {
        super.map(dto, entity);
        entity.setInuId(dto.getInuId());
        entity.setIndividual(individualRepository.get(dto.getIndId()));
        entity.setVerificationCode(dto.getVerificationCode());
        entity.setActive(dto.isActive());
        entity.setLastResetFailureDate(dto.getLastResetFailureDate());
        entity.setLastLoginSuccessDate(dto.getLastLoginSuccessDate());
        entity.setLastLoginFailureDate(dto.getLastLoginFailureDate());
        entity.setLoginFailureAttempts(dto.getLoginFailureAttempts());
        entity.setResetFailureAttempts(dto.getResetFailureAttempts());
    }
}