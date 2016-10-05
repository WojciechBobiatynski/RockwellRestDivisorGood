package pl.sodexo.it.gryf.dao.impl.crud.repository.security;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.exception.authentication.GryfAuthenticationException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfBadCredentialsException;
import pl.sodexo.it.gryf.dao.api.crud.repository.security.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.tryFind;

/**
 * Implementacja dao do operacji autentykacji uzytkownika.
 * 
 * Created by akuchna on 2016-09-26.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class UserRepositoryImpl implements UserRepository {

    private static final int ERROR_CODE_BAD_CREDENTIALS = 1017;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Override
    public List<String> findRolesForLogin(String login, String password) {
        try (Connection dsConnection = dataSource.getConnection(); Connection ignored = DriverManager.getConnection(dsConnection.getMetaData().getURL(), login, password)) {
            return findRolesForLogin(login);
        } catch (Exception e) {
            return handleAuthenticationException(e);
        }
    }

    private List<String> findRolesForLogin(String login) {
        return entityManager.createNativeQuery("" + 
                "select aug_id " + 
                "from EAGLE.ADM_USER_IN_ROLES " + 
                "natural left join EAGLE.ADM_GROUPS_IN_ROLES " + 
                "where upper(aur_id) = upper(?) " + 
                "and aug_id is not null")
                .setParameter(1, login)
                .getResultList();
    }

    private List<String> handleAuthenticationException(Exception e) {
        handleIfBadCredentials(e); 
        throw new GryfAuthenticationException("Blad podczas autentykacji", e);
    }

    private void handleIfBadCredentials(Exception e) {
        Optional<SQLException> readOnlyException = tryFind(filter(Throwables.getCausalChain(e), SQLException.class), ex -> ex.getErrorCode() == ERROR_CODE_BAD_CREDENTIALS);
        if (!readOnlyException.isPresent()) return;
        
        throw new GryfBadCredentialsException("Nieprawidlowy uzytkownik lub haslo", e);
    }
}
