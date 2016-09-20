package pl.sodexo.it.gryf.root.repository.publicbenefits.grantprograms;

import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramLimit;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantProgramLimitRepository extends GenericRepository<GrantProgramLimit, Long> {

    List<GrantProgramLimit> findByGrantProgramEntSizeLimitTypeInDate(Long grantProgramId, String enterpriseSizeId, GrantProgramLimit.LimitType limitType, Date date);
}
