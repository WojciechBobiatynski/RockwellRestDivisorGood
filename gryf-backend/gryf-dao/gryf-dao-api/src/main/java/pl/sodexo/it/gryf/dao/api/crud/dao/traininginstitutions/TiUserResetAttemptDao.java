package pl.sodexo.it.gryf.dao.api.crud.dao.traininginstitutions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TiUserResetAttempt;

import java.util.Date;
import java.util.List;

/**
 * Dao reprezentujące operacje na żądania użytkonwików o reset hasła
 *
 * Created by akmiecinski on 26.10.2016.
 */
@Transactional(propagation = Propagation.MANDATORY)
public interface TiUserResetAttemptDao extends JpaRepository<TiUserResetAttempt, String> {

    @Query("SELECT res FROM TiUserResetAttempt res JOIN TrainingInstitutionUser user on res.trainingInstitutionUser = user "
            + "WHERE res.expiryDate >= :now AND user.id = :tiuId")
    List<TiUserResetAttempt> findCurrentByTrainingInstitutionId(@Param("tiuId") Long tiuId, @Param("now") Date now);

}
