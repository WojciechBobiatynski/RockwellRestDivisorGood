package pl.sodexo.it.gryf.service.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.password.ChangePasswordDto;
import pl.sodexo.it.gryf.common.exception.authentication.GryfBadCredentialsException;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.service.api.password.PasswordService;
import pl.sodexo.it.gryf.dao.api.crud.repository.security.UserRepository;

import static pl.sodexo.it.gryf.common.dto.user.GryfUser.getLoggedUser;

/**
 * Serwis realizujący operacje związane ze zmianą hasła
 *
 * Created by akmiecinski on 16.05.2017.
 */
@Service
@Transactional
public class PasswordServiceImpl implements PasswordService{

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        String username = getLoggedUser().getUser().getLogin();
        changeIfNewPasswordTheSame(changePasswordDto);
        changeIfOldPasswordCorrect(username, changePasswordDto);
        gryfPLSQLRepository.changePassword(username, changePasswordDto);
    }

    private void changeIfOldPasswordCorrect(String username, ChangePasswordDto changePasswordDto) {
        try {
            userRepository.findRolesForLogin(username, changePasswordDto.getCurrentPassword());
        }
        catch (GryfBadCredentialsException e) {
            throw new GryfBadCredentialsException("Dotychczasowe hasło jest nieprawidłowe.", e);
        }
    }

    private void changeIfNewPasswordTheSame(ChangePasswordDto changePasswordDto)
    {
        if (changePasswordDto.getConfirmPassword().equals(changePasswordDto.getNewPassword() ) ) { }
        throw new GryfBadCredentialsException("Hasła nie są zgodne.");
    }

}
