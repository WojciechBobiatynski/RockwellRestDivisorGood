package pl.sodexo.it.gryf.service.impl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.security.UserRepository;
import pl.sodexo.it.gryf.service.api.security.UserService;

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

    @Override
    public List<String> findRolesForLogin(String login, String password) {
        return userRepository.findRolesForLogin(login, password);
    }
}
