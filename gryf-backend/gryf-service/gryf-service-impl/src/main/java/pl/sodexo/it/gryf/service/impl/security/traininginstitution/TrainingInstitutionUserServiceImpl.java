package pl.sodexo.it.gryf.service.impl.security.traininginstitution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.common.exception.NoAppropriateData;
import pl.sodexo.it.gryf.common.exception.verification.GryfInvalidTokenException;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.operator.OperatorUserRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TiUserResetAttemptRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionUserRepository;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TiUserResetAttempt;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;
import pl.sodexo.it.gryf.service.api.security.trainingInstitutions.TrainingInstitutionUserService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.security.traininginstitutions.GryfTiUserDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.security.traininginstitutions.TrainingInstitutionUserEntityMapper;
import pl.sodexo.it.gryf.service.utils.FoUserParameterCodes;

/**
 * Implementacja serwius zajmującego się operacjami związanymi z użytkownikiem Usługodawcy
 *
 * Created by akmiecinski on 24.10.2016.
 */
@Service
@Transactional
public class TrainingInstitutionUserServiceImpl implements TrainingInstitutionUserService{

    @Autowired
    private TrainingInstitutionUserRepository trainingInstitutionUserRepository;

    @Autowired
    private TrainingInstitutionUserEntityMapper trainingInstitutionUserEntityMapper;

    @Autowired
    private GryfTiUserDtoMapper gryfTiUserDtoMapper;

    @Autowired
    private TiUserResetAttemptRepository tiUserResetAttemptRepository;

    @Autowired
    private OperatorUserRepository operatorUserRepository;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Override
    public GryfTiUserDto saveTiUser(GryfTiUserDto gryfTiUserDto) {
        TrainingInstitutionUser entity = gryfTiUserDtoMapper.convert(gryfTiUserDto);
        return trainingInstitutionUserEntityMapper.convert(trainingInstitutionUserRepository.saveOrUpdate(entity, entity.getId()));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GryfTiUserDto saveAndFlushTiUserInNewTransaction(GryfTiUserDto gryfTiUserDto) {
        return saveTiUser(gryfTiUserDto);
    }

    @Override
    public GryfTiUserDto findTiUserByLogin(String login) {
        return trainingInstitutionUserEntityMapper.convert(trainingInstitutionUserRepository.findByLoginIgnoreCase(login));
    }

    @Override
    public GryfTiUserDto findTiUserByEmail(String email) {
        return trainingInstitutionUserEntityMapper.convert(trainingInstitutionUserRepository.findByEmailIgnoreCase(email));
    }

    @Override
    public GryfTiUserDto findUserByTurIdAndSaveNewPassword(String turId, String password) {
        TiUserResetAttempt tiUserResetAttempt = tiUserResetAttemptRepository.get(turId);
        if(tiUserResetAttempt == null){
            throw new GryfInvalidTokenException("Niepoprawny token");
        }
        TrainingInstitutionUser user = tiUserResetAttempt.getTrainingInstitutionUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
        tiUserResetAttempt.setUsed(true);

        user = trainingInstitutionUserRepository.update(user, user.getId());
        return trainingInstitutionUserEntityMapper.convert(user);
    }

    @Override
    public boolean isPasswordStrong(String password){
        String strongPasswordRegexp = applicationParameters.getStrongPasswordRegexp();
        return GryfStringUtils.isRegexpMatch(strongPasswordRegexp, password);
    }

    @Override
    public GryfTiUserDto findTiUserForFoLogin(String foUserLogin) {
        String tiIdParam = operatorUserRepository
                .findUserParameterValueByLoginAndKey(foUserLogin, FoUserParameterCodes.TI_USER_ID.toString());
        if (tiIdParam == null) {
            throw new NoAppropriateData("Brak powiązanego użytkownika Usługodawcy.");
        }
        TrainingInstitutionUser tiUser = trainingInstitutionUserRepository.get(Long.parseLong(tiIdParam));
        if (tiUser == null) {
            throw new NoAppropriateData("Użytkownik nie istnieje.");
        }
        return trainingInstitutionUserEntityMapper.convert(tiUser);
    }
}
