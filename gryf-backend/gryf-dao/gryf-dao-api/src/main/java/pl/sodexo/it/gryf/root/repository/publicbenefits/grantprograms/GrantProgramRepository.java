package pl.sodexo.it.gryf.root.repository.publicbenefits.grantprograms;

import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantProgramRepository extends GenericRepository<GrantProgram, Long> {

    List<GrantProgram> findProgramsByDate(Date date);
}