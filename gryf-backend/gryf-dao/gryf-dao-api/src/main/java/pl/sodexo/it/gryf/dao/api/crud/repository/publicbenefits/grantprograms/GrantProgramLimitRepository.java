package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramLimit;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantProgramLimitRepository extends GenericRepository<GrantProgramLimit, Long> {

    List<GrantProgramLimit> findByGrantProgramEntSizeLimitTypeInDate(Long grantProgramId, String enterpriseSizeId, GrantProgramLimit.LimitType limitType, Date date);
}
