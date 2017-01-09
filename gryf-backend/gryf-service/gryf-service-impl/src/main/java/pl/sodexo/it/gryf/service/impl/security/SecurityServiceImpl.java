package pl.sodexo.it.gryf.service.impl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.dao.api.search.dao.security.SecuritySearchDao;
import pl.sodexo.it.gryf.service.api.security.SecurityService;

import java.util.List;

/**
 * Implementacja serwisu realizujacy uslugi dotyczące uwierzytelniania uzytkownika po stronie aplikacji
 *
 * Created by akmiecinski on 21.10.2016.
 */
@Service
@Transactional
public class SecurityServiceImpl implements SecurityService{

    @Autowired
    private SecuritySearchDao securitySearchDao;

    @Override
    public GryfIndUserDto findIndUserByPesel(String pesel) {
        return securitySearchDao.findIndUserByPesel(pesel);
    }

    @Override
    public List<RoleDto> findRolesForTiUser() {
        return securitySearchDao.findRolesForTiUser();
    }

    @Override
    public List<RoleDto> findRolesForIndUser() {
        return securitySearchDao.findRolesForIndUser();
    }

    @Override
    public List<RoleDto> findRolesForIndividualUser(Long individualUserId) {
        return securitySearchDao.findRolesForIndividualUser(individualUserId);
    }
}
