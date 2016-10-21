package pl.sodexo.it.gryf.service.impl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserHeadDto;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;
import pl.sodexo.it.gryf.common.exception.authentication.GryfBadCredentialsException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfPasswordExpiredException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfUserNotActiveException;
import pl.sodexo.it.gryf.dao.api.crud.dao.trainingInstitutions.TrainingInstitutionUserDao;
import pl.sodexo.it.gryf.dao.api.crud.repository.security.UserRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.security.SecuritySearchDao;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;
import pl.sodexo.it.gryf.service.api.security.UserService;

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
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecuritySearchDao securitySearchDao;

    @Autowired
    private TrainingInstitutionUserDao trainingInstitutionUserDao;

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
        TrainingInstitutionUser user = trainingInstitutionUserDao.findByLogin(login);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new GryfBadCredentialsException("Niepoprawny login lub/i hasło");
        }

        if (!user.getIsActive()) {
            throw new GryfUserNotActiveException("Twoje konto jest nieaktywne. Zgłoś sie do administratora");
        }

        Date currentDate = new Date();

        if (user.getPasswordExpirationDate() != null && currentDate.after(user.getPasswordExpirationDate())) {
            throw new GryfPasswordExpiredException("Hasło wygasło");
        }

    }

    @Override
    public void updateLastLoginDateTi (GryfTiUser gryfTiUser){
        TrainingInstitutionUser tiUser = trainingInstitutionUserDao.findByLogin(gryfTiUser.getUsername());
        tiUser.setLastLoginDate(new Date());
        trainingInstitutionUserDao.save(tiUser);
    }

    private void authenticateIndUser(String pesel, String verificationCode) {
        GryfIndUserHeadDto user = securitySearchDao.findIndUserByPesel(pesel);

        if(user == null){
            throw new GryfBadCredentialsException("Niepoprawny PESEL");
        }

        if (!user.isActive()) {
            throw new GryfUserNotActiveException("Twoje konto jest nieaktywne. Zgłoś sie do administratora");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user == null || !passwordEncoder.matches(verificationCode, user.getVerificationCode())) {
            throw new GryfBadCredentialsException("Niepoprawny PESEL lub/i hasło");
        }

    }
}
