package pl.sodexo.it.gryf.service.impl.security;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.security.GryfBlockableUserDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;
import pl.sodexo.it.gryf.common.exception.authentication.GryfBadCredentialsException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfPasswordExpiredException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfUserNotActiveException;
import pl.sodexo.it.gryf.common.user.GryfBlockableUserVisitor;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionUserRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.security.UserRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.security.SecuritySearchDao;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;
import pl.sodexo.it.gryf.service.api.security.UserService;
import pl.sodexo.it.gryf.service.api.security.individuals.IndividualUserService;
import pl.sodexo.it.gryf.service.api.security.trainingInstitutions.TrainingInstitutionUserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Implementacja serwisu realizujacego uslugi dotyczace autentykacji uzytkownika.
 *
 * Created by akuchna on 2016-09-26.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecuritySearchDao securitySearchDao;

    @Autowired
    private TrainingInstitutionUserRepository trainingInstitutionUserRepository;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private IndividualUserService individualUserService;

    @Autowired
    private TrainingInstitutionUserService trainingInstitutionUserService;

    @Override
    public List<String> findPrivilegesForFoLogin(String login, String password) {
        return userRepository.findRolesForLogin(login, password);
    }

    @Override
    public List<String> findPrivilegesForTiLogin(String login, String verificationCode) {
        authenticateTiUser(login, verificationCode);
        return securitySearchDao.findTiUserPrivileges(login);
    }

    @Override
    public List<String> findPrivilegesForIndPesel(String pesel, String password) {
        authenticateIndUser(pesel, password);
        return securitySearchDao.findIndUserPrivileges(pesel);
    }

    private void authenticateTiUser(String login, String password) {
        GryfTiUserDto user = trainingInstitutionUserService.findTiUserByLogin(login);

        if (user == null) {
            LOGGER.warn("Niepoprawny login={}", login);
            throw new GryfBadCredentialsException("Niepoprawny login lub/i hasło");
        }

        unlockUserInNewTransaction(user);

        if (!user.isActive()) {
            LOGGER.warn("Konto jest nieaktywne, login={}", login);
            throw new GryfUserNotActiveException("Twoje konto jest nieaktywne. Zgłoś sie do administratora");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            LOGGER.warn("Niepoprawne hasło, login={}", login);
            throw new GryfBadCredentialsException("Niepoprawny login lub/i hasło");
        }

        Date currentDate = new Date();

        if (user.getPasswordExpirationDate() != null && currentDate.after(user.getPasswordExpirationDate())) {
            LOGGER.warn("Hasło wygasło, login={}", login);
            throw new GryfPasswordExpiredException("Hasło wygasło");
        }

    }

    @Override
    public void updateTiAfterSuccessLogin(GryfTiUser gryfTiUser) {
        TrainingInstitutionUser tiUser = trainingInstitutionUserRepository.findByLoginIgnoreCase(gryfTiUser.getUsername());
        tiUser.setLastLoginSuccessDate(new Date());
        tiUser.setLoginFailureAttempts(GryfConstants.DEFAULT_LOGIN_FAILURE_ATTEMPTS_NUMBER);
        trainingInstitutionUserRepository.update(tiUser, tiUser.getId());
    }

    @Override
    public Long findTrainingInstitutionIdForTiUser(String tiUserLogin) {
        return securitySearchDao.findTrainingInstitutionIdForTiUser(tiUserLogin);
    }

    private void authenticateIndUser(String pesel, String verificationCode) {
        //TODO pobierać póxniej przy pomocy spring data jpa dla zachowania spójności - nie będzie potrzeby decryptowania hasła
        GryfIndUserDto user = securitySearchDao.findIndUserByPesel(pesel);
        if (user == null) {
            LOGGER.warn("Niepoprawny PESEL={}", pesel);
            throw new GryfBadCredentialsException("Niepoprawny PESEL lub/i hasło");
        }
        user.setRoles(Sets.newHashSet(securitySearchDao.findRolesForIndividualUser(user.getInuId())));
        user.setVerificationCode(AEScryptographer.decrypt(user.getVerificationCode()));

        unlockUserInNewTransaction(user);

        if (!user.isActive()) {
            LOGGER.warn("Konto jest nieaktywne, PESEL={}", pesel);
            throw new GryfUserNotActiveException("Twoje konto jest nieaktywne. Zgłoś sie do administratora");
        }

        if (!verificationCode.equals(user.getVerificationCode())){
            LOGGER.warn("Niepoprawne hasło, PESEL={}", pesel);
            throw new GryfBadCredentialsException("Niepoprawny PESEL lub/i hasło");
        }

    }

    @Override
    public GryfBlockableUserDto unlockUser(GryfBlockableUserDto user) {
        return user.accept(new GryfBlockableUserVisitor<GryfBlockableUserDto>() {

            @Override
            public GryfBlockableUserDto visitInd(GryfIndUserDto gryfIndUserDto) {
                unlockIndUser(gryfIndUserDto);
                return individualUserService.updateIndUser(gryfIndUserDto);
            }

            @Override
            public GryfBlockableUserDto visitTi(GryfTiUserDto gryfTiUserDto) {
                if (gryfTiUserDto.getLastLoginFailureDate() == null) {
                    return gryfTiUserDto;
                }
                if (isBlockByLoginFailureNotActive(gryfTiUserDto) && gryfTiUserDto.getLoginFailureAttempts() >= applicationParameters.getMaxLoginFailureAttempts()) {
                    gryfTiUserDto.setLoginFailureAttempts(GryfConstants.DEFAULT_LOGIN_FAILURE_ATTEMPTS_NUMBER);
                    gryfTiUserDto.setActive(true);
                    return trainingInstitutionUserService.saveTiUser(gryfTiUserDto);
                }
                return gryfTiUserDto;
            }
        });
    }

    private GryfBlockableUserDto unlockUserInNewTransaction(GryfBlockableUserDto user) {
        return user.accept(new GryfBlockableUserVisitor<GryfBlockableUserDto>() {

            @Override
            public GryfBlockableUserDto visitInd(GryfIndUserDto gryfIndUserDto) {
                unlockIndUser(gryfIndUserDto);
                return individualUserService.updateIndUserInNewTransaction(gryfIndUserDto);
            }

            @Override
            public GryfBlockableUserDto visitTi(GryfTiUserDto gryfTiUserDto) {
                if (gryfTiUserDto.getLastLoginFailureDate() == null) {
                    return gryfTiUserDto;
                }
                if (isBlockByLoginFailureNotActive(gryfTiUserDto) && gryfTiUserDto.getLoginFailureAttempts() >= applicationParameters.getMaxLoginFailureAttempts()) {
                    gryfTiUserDto.setLoginFailureAttempts(GryfConstants.DEFAULT_LOGIN_FAILURE_ATTEMPTS_NUMBER);
                    gryfTiUserDto.setActive(true);
                    return trainingInstitutionUserService.saveAndFlushTiUserInNewTransaction(gryfTiUserDto);
                }
                return gryfTiUserDto;
            }
        });
    }

    private GryfIndUserDto unlockIndUser(GryfIndUserDto user) {
        if (isBlockByResetFailureNotActive(user) && isBlockByLoginFailureNotActive(user)) {
            user.setActive(true);
            user.setResetFailureAttempts(GryfConstants.DEFAULT_RESET_FAILURE_ATTEMPTS_NUMBER);
            user.setLoginFailureAttempts(GryfConstants.DEFAULT_LOGIN_FAILURE_ATTEMPTS_NUMBER);
        }
        return user;
    }

    private boolean isBlockByResetFailureNotActive(GryfIndUserDto user) {
        if (user.getLastResetFailureDate() == null) {
            return true;
        }
        LocalDateTime lastResetFailureDate = LocalDateTime.ofInstant(user.getLastResetFailureDate().toInstant(), ZoneId.systemDefault());
        return lastResetFailureDate.plusMinutes(applicationParameters.getIndUserResetBlockMinutes()).isBefore(LocalDateTime.now());
    }

    private boolean isBlockByLoginFailureNotActive(GryfBlockableUserDto user) {
        if (user.getLastLoginFailureDate() == null) {
            return true;
        }
        LocalDateTime lastLoginFailureDate = LocalDateTime.ofInstant(user.getLastLoginFailureDate().toInstant(), ZoneId.systemDefault());
        return lastLoginFailureDate.plusMinutes(applicationParameters.getUserLoginBlockMinutes()).isBefore(LocalDateTime.now());
    }
}
