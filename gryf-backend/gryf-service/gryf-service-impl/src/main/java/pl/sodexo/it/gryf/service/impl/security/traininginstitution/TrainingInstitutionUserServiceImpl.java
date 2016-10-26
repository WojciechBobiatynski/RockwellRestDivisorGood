package pl.sodexo.it.gryf.service.impl.security.traininginstitution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;
import pl.sodexo.it.gryf.dao.api.crud.dao.traininginstitutions.TrainingInstitutionUserDao;
import pl.sodexo.it.gryf.model.security.trainingInstitutions.TrainingInstitutionUser;
import pl.sodexo.it.gryf.service.api.security.trainingInstitutions.TrainingInstitutionUserService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.security.traininginstitutions.GryfTiUserDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.security.traininginstitutions.TrainingInstitutionUserEntityMapper;

/**
 * Implementacja serwius zajmującego się operacjami związanymi z użytkownikiem instytucji szkoleniowej
 *
 * Created by akmiecinski on 24.10.2016.
 */
@Service
@Transactional
public class TrainingInstitutionUserServiceImpl implements TrainingInstitutionUserService{

    @Autowired
    private TrainingInstitutionUserDao trainingInstitutionUserDao;

    @Autowired
    private TrainingInstitutionUserEntityMapper trainingInstitutionUserEntityMapper;

    @Autowired
    private GryfTiUserDtoMapper gryfTiUserDtoMapper;

    @Override
    public GryfTiUserDto saveTiUser(GryfTiUserDto gryfTiUserDto) {
        TrainingInstitutionUser entity = gryfTiUserDtoMapper.convert(gryfTiUserDto);
        return trainingInstitutionUserEntityMapper.convert(trainingInstitutionUserDao.save(entity));
    }

    @Override
    public GryfTiUserDto saveAndFlushTiUser(GryfTiUserDto gryfTiUserDto) {
        TrainingInstitutionUser entity = gryfTiUserDtoMapper.convert(gryfTiUserDto);
        return trainingInstitutionUserEntityMapper.convert(trainingInstitutionUserDao.saveAndFlush(entity));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GryfTiUserDto saveAndFlushTiUserInNewTransaction(GryfTiUserDto gryfTiUserDto) {
        return saveAndFlushTiUser(gryfTiUserDto);
    }

    @Override
    public GryfTiUserDto findTiUserByLogin(String login) {
        return trainingInstitutionUserEntityMapper.convert(trainingInstitutionUserDao.findByLogin(login));
    }

    @Override
    public GryfTiUserDto findTiUserByEmail(String email) {
        return trainingInstitutionUserEntityMapper.convert(trainingInstitutionUserDao.findByEmail(email));
    }
}
