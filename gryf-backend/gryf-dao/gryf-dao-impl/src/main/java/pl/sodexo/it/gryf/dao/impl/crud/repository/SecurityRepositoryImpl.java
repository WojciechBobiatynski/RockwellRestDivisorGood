package pl.sodexo.it.gryf.dao.impl.crud.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.SecurityRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by akmiecinski on 2016-09-22.
 */
@Repository
public class SecurityRepositoryImpl implements SecurityRepository{

    @PersistenceContext
    EntityManager em;

    @Autowired
    DataSource dataSource;

    @Override
    public List<String> findRolesForLogin(String login) {
        return em.createNativeQuery("select aug_id from EAGLE.ADM_USER_IN_ROLES natural left join EAGLE.ADM_GROUPS_IN_ROLES where upper(aur_id) = upper(?) and aug_id is not null")
                .setParameter(1, login)
                .getResultList();
    }
}
