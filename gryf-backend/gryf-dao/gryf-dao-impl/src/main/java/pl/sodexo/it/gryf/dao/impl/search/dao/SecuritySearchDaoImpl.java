package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.dao.api.search.dao.security.SecuritySearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.security.SecuritySearchMapper;

import java.util.List;

/**
 * Implementacja Dao do wyszukiwania uprawnień i ról użytkowników
 *
 * Created by akmiecinski on 18.10.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class SecuritySearchDaoImpl implements SecuritySearchDao {

    @Autowired
    private SecuritySearchMapper securitySearchMapper;

    @Override
    public List<String> findTiUserPrivileges(String login) {
        return securitySearchMapper.findTiUserPrivileges(new UserCriteria(), login);
    }

    @Override
    public List<String> findIndUserPrivileges(String pesel) {
        return securitySearchMapper.findIndUserPrivileges(new UserCriteria(), pesel);
    }

    @Override
    public GryfIndUserDto findIndUserByPesel(String pesel) {
        return securitySearchMapper.findIndUserByPesel(new UserCriteria(), pesel);
    }
}
