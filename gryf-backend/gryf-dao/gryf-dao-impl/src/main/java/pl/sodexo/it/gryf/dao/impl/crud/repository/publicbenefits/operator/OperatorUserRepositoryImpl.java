package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.operator.OperatorUserRepository;

@Repository
@Transactional
public class OperatorUserRepositoryImpl implements OperatorUserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperatorUserRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Override
    public String findUserParameterValueByLoginAndKey(String login, String key) {
        try (Connection connection = dataSource.getConnection()) {
            Query query = entityManager.createNativeQuery("" +
                    "SELECT value FROM EAGLE.ADM_USER_PARAMETERS " +
                    "WHERE upper(aur_id) = upper(?) " +
                    "AND upper(code) = upper(?)");
            query.setParameter(1, login).setParameter(2, key);
            return (String) query.getSingleResult();
        } catch (NoResultException e) {
            LOGGER.info("Brak parametru \"" + key + "\" dla użytkownika \"" + login + "\".");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
