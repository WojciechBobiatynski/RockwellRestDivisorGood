package pl.sodexo.it.gryf.service.impl.security.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.authentication.AEScryptographer;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.common.dto.user.GryfIndUser;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.dao.api.crud.dao.individuals.IndividualUserDao;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;
import pl.sodexo.it.gryf.service.api.security.individuals.IndividualUserService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.individuals.IndividualDtoMapper;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.security.individuals.GryfIndUserDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.security.individuals.IndividualUserEntityMapper;

import java.util.Date;

/**
 * Implementacja serwius zajmującego się operacjami związanymi z użytkownikiem osoby fizycznej
 *
 * Created by akmiecinski on 21.10.2016.
 */
@Service
@Transactional
public class IndividualUserServiceImpl implements IndividualUserService {

    @Autowired
    private IndividualUserDao individualUserDao;

    @Autowired
    private IndividualUserEntityMapper individualUserEntityMapper;

    @Autowired
    private GryfIndUserDtoMapper gryfIndUserDtoMapper;

    @Autowired
    private IndividualDtoMapper individualDtoMapper;

    @Override
    public GryfIndUserDto findByPeselWithVerEmail(String pesel) {
        return individualUserEntityMapper.convert(individualUserDao.findByPeselWithVerEmail(pesel));
    }

    @Override
    public GryfIndUserDto findByPesel(String pesel) {
        return individualUserEntityMapper.convert(individualUserDao.findByIndividual_Pesel(pesel));
    }

    @Override
    public GryfIndUserDto saveIndUser(GryfIndUserDto gryfIndUserDto) {
        IndividualUser entity = gryfIndUserDtoMapper.convert(gryfIndUserDto);
        return individualUserEntityMapper.convert(individualUserDao.save(entity));
    }

    @Override
    public GryfIndUserDto saveAndFlushIndUser(GryfIndUserDto gryfIndUserDto) {
        IndividualUser entity = gryfIndUserDtoMapper.convert(gryfIndUserDto);
        return individualUserEntityMapper.convert(individualUserDao.saveAndFlush(entity));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GryfIndUserDto saveAndFlushIndUserInNewTransaction(GryfIndUserDto gryfIndUserDto) {
        return saveAndFlushIndUser(gryfIndUserDto);
    }

    @Override
    public GryfIndUserDto createAndSaveNewUser(IndividualDto individualDto, String verificationCode) {
        IndividualUser entity = new IndividualUser();
        entity.setVerificationCode(AEScryptographer.encrypt(verificationCode));
        entity.setIndividual(individualDtoMapper.convert(individualDto));
        entity.setActive(true);
        return individualUserEntityMapper.convert(individualUserDao.save(entity));
    }

    @Override
    public void updateIndAfterSuccessLogin(GryfIndUser gryfIndUser) {
        IndividualUser user = individualUserDao.findByIndividual_Pesel(gryfIndUser.getUsername());
        user.setLastLoginSuccessDate(new Date());
        user.setLoginFailureAttempts(GryfConstants.DEFAULT_LOGIN_FAILURE_ATTEMPTS_NUMBER);
        individualUserDao.save(user);
    }

    @Override
    public GryfIndUserDto saveNewVerificationCodeForIndividual(Long individualId, String verificationCode) {
        IndividualUser user = individualUserDao.findByIndividual_Id(individualId);
        user.setVerificationCode(AEScryptographer.encrypt(verificationCode));
        return individualUserEntityMapper.convert(individualUserDao.save(user));
    }

}
