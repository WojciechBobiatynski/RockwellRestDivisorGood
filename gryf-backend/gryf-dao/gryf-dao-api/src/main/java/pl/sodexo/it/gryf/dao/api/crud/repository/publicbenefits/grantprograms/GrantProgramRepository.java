package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantProgramRepository extends GenericRepository<GrantProgram, Long> {

    List<GrantProgram> findProgramsByDate(Date date);
}
