package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantProgramParamRepository extends GenericRepository<GrantProgramParam, Long> {

    List<GrantProgramParam> findByGrantProgramInDate(Long grantProgramId, String paramTypeId, Date date);
}
