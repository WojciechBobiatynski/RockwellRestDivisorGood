package pl.sodexo.it.gryf.service.impl.security.traininginstitution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.TiUserResetAttemptDto;
import pl.sodexo.it.gryf.common.exception.verification.GryfInvalidTokenException;
import pl.sodexo.it.gryf.common.exception.verification.GryfResetLinkNotActive;
import pl.sodexo.it.gryf.dao.api.crud.dao.traininginstitutions.TiUserResetAttemptDao;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TiUserResetAttempt;
import pl.sodexo.it.gryf.service.api.security.trainingInstitutions.TiUserResetAttemptService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.security.traininginstitutions.TiUserResetAttemptDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.security.traininginstitutions.TiUserResetAttemptEntityMapper;

import java.util.Date;
import java.util.UUID;

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

    @Autowired
    private TiUserResetAttemptDtoMapper tiUserResetAttemptDtoMapper;

    @Override
    public void disableActiveAttemptOfTiUser(Long tiuId) {
        TiUserResetAttempt tiUserResetAttempt = tiUserResetAttemptDao.findCurrentByTrainingInstitutionId(tiuId, new Date());
        if(tiUserResetAttempt != null){
            tiUserResetAttempt.setExpiryDate(new Date());
            tiUserResetAttemptDao.save(tiUserResetAttempt);
        }
    }

    @Override
    public String createNewLink() {
        String newLink = UUID.randomUUID().toString();
        while(tiUserResetAttemptDao.findByTurId(newLink) != null){
            newLink = UUID.randomUUID().toString();
        }
        return newLink;
    }

    @Override
    public TiUserResetAttemptDto saveTiUserResetAttempt(TiUserResetAttemptDto tiUserResetAttemptDto) {
        TiUserResetAttempt entity = tiUserResetAttemptDtoMapper.convert(tiUserResetAttemptDto);
        return tiUserResetAttemptEntityMapper.convert(tiUserResetAttemptDao.save(entity));
    }

    @Override
    public void checkIfResetAttemptStillActive(String turId) {
        TiUserResetAttempt tiUserResetAttempt = tiUserResetAttemptDao.findByTurId(turId);
        if(tiUserResetAttempt == null){
            throw new GryfInvalidTokenException("Niepoprawny token");
        }
        if(tiUserResetAttempt.getExpiryDate().before(new Date())){
            throw new GryfResetLinkNotActive("Link do zmiany hasła wygasł");
        }
        if(tiUserResetAttempt.isUsed()){
            throw new GryfResetLinkNotActive("Link do zmiany hasła został już wykorzystany");
        }
    }
}
