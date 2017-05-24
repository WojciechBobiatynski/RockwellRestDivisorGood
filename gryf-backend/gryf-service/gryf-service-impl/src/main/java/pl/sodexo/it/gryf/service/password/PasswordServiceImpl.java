package pl.sodexo.it.gryf.service.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.password.ChangePasswordDto;
import pl.sodexo.it.gryf.common.exception.authentication.GryfBadCredentialsException;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.security.UserRepository;
import pl.sodexo.it.gryf.service.api.password.PasswordService;

import static pl.sodexo.it.gryf.common.dto.user.GryfUser.getLoggedUser;

/**
 * Serwis realizujący operacje związane ze zmianą hasła
 *
 * Created by akmiecinski on 16.05.2017.
 */
@Service
@Transactional
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        ckeckIfNewPasswordTheSame(changePasswordDto);
        String username = getLoggedUser().getUser().getLogin();
        checkIfOldPasswordCorrect(username, changePasswordDto);
        gryfPLSQLRepository.changePassword(username, changePasswordDto);
    }

    private void checkIfOldPasswordCorrect(String username, ChangePasswordDto changePasswordDto) {
        userRepository.checkIfOldPasswordCorrect(username, changePasswordDto.getCurrentPassword());
    }

    private void ckeckIfNewPasswordTheSame(ChangePasswordDto changePasswordDto) {
        if (!changePasswordDto.getConfirmPassword().equals(changePasswordDto.getNewPassword()))
            throw new GryfBadCredentialsException("Hasła nie są zgodne.");
    }

}
