package pl.sodexo.it.gryf.dao.api.crud.repository.asynch;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.asynch.JobType;

import java.util.List;

/**
 * Interfejs dostepu do danych w tabeli JobType
 */
@Repository
@Transactional
public interface JobTypeRepository extends GenericRepository<JobType, Long> {

    JobType findByGrantProgramIdAndName(Long grantProgramId, String name);

    JobType findByName(String name);

    List<JobType> findByGrantProgramId(long grantProgramId);
}
