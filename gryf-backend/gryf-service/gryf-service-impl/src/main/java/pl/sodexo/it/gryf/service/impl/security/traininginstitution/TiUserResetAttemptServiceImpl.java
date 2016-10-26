package pl.sodexo.it.gryf.service.impl.security.traininginstitution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.TiUserResetAttemptDto;
import pl.sodexo.it.gryf.dao.api.crud.dao.traininginstitutions.TiUserResetAttemptDao;
import pl.sodexo.it.gryf.service.api.security.trainingInstitutions.TiUserResetAttemptService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.security.traininginstitutions.TiUserResetAttemptEntityMapper;

import java.util.Date;
import java.util.List;

/**
 * Implementacja serwisu do operacji na żądaniach resetu hasła dla użytkownika instytucji szkoleniowej
 *
 * Created by akmiecinski on 26.10.2016.
 */
@Service
@Transactional
public class TiUserResetAttemptServiceImpl implements TiUserResetAttemptService {

    @Autowired
    private TiUserResetAttemptDao tiUserResetAttemptDao;

    @Autowired
    private TiUserResetAttemptEntityMapper tiUserResetAttemptEntityMapper;

    @Override
    public List<TiUserResetAttemptDto> findCurrentByTrainingInstitutionId(Long tiuId) {
        return tiUserResetAttemptEntityMapper.convert(tiUserResetAttemptDao.findCurrentByTrainingInstitutionId(tiuId, new Date()));
    }
}
