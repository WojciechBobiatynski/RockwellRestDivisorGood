package pl.sodexo.it.gryf.service.mapping.entitytodto.security.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;

/**
 * Komponent mapujący encję osoby fizycznej na dto
 *
 * Created by akmiecinski on 21.10.2016.
 */
@Component
public class IndividualUserEntityMapper extends VersionableEntityMapper<IndividualUser, GryfIndUserDto> {

    @Autowired
    private ApplicationParameters applicationParameters;

    @Override
    protected GryfIndUserDto initDestination() {
        return new GryfIndUserDto();
    }

    @Override
    public void map(IndividualUser entity, GryfIndUserDto dto) {
        super.map(entity, dto);
        dto.setInuId(entity.getInuId());
        dto.setIndId(entity.getIndividual().getId());
        dto.setPesel(entity.getIndividual().getPesel());
        dto.setActive(entity.isActive());
        dto.setVerificationCode(entity.getVerificationCode());
        dto.setLastLoginFailureDate(entity.getLastLoginFailureDate());
        dto.setLastLoginSuccessDate(entity.getLastLoginSuccessDate());
        dto.setLastResetFailureDate(entity.getLastResetFailureDate());
        dto.setLoginFailureAttempts(entity.getLoginFailureAttempts());
        dto.setResetFailureAttempts(entity.getResetFailureAttempts());

        //Ze względu na unmodifiableList nie na streamach
        for(IndividualContact ind : entity.getIndividual().getContacts()){
            if(applicationParameters.getVerEmailContactType().equals(ind.getContactType().getType())){
                dto.setVerificationEmail(ind.getContactData());
            }
        }
    }
}
