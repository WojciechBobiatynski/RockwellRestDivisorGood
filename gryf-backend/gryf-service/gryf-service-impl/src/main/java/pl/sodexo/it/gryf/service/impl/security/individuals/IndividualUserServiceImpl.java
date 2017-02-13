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
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualUserRepository;
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
    private IndividualUserRepository individualUserRepository;

    @Autowired
    private IndividualUserEntityMapper individualUserEntityMapper;

    @Autowired
    private GryfIndUserDtoMapper gryfIndUserDtoMapper;

    @Autowired
    private IndividualDtoMapper individualDtoMapper;

    @Override
    public GryfIndUserDto findByPeselWithVerEmail(String pesel) {
        IndividualUser iu = individualUserRepository.findByPeselWithVerEmail(pesel);
        return individualUserEntityMapper.convert(iu);
    }

    @Override
    public GryfIndUserDto findByPesel(String pesel) {
        return individualUserEntityMapper.convert(individualUserRepository.findByIndividualPesel(pesel));
    }

    @Override
    public GryfIndUserDto updateIndUser(GryfIndUserDto gryfIndUserDto) {
        IndividualUser entity = gryfIndUserDtoMapper.convert(gryfIndUserDto);
        return individualUserEntityMapper.convert(individualUserRepository.update(entity, entity.getInuId()));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GryfIndUserDto updateIndUserInNewTransaction(GryfIndUserDto gryfIndUserDto) {
        return updateIndUser(gryfIndUserDto);
    }

    @Override
    public GryfIndUserDto createAndSaveNewUser(IndividualDto individualDto, String verificationCode) {
        IndividualUser entity = new IndividualUser();
        entity.setVerificationCode(AEScryptographer.encrypt(verificationCode));
        entity.setIndividual(individualDtoMapper.convert(individualDto));
        entity.setActive(true);
        return individualUserEntityMapper.convert(individualUserRepository.save(entity));
    }

    @Override
    public void updateIndAfterSuccessLogin(GryfIndUser gryfIndUser) {
        IndividualUser user = individualUserRepository.findByIndividualPesel(gryfIndUser.getUsername());
        user.setLastLoginSuccessDate(new Date());
        user.setLoginFailureAttempts(GryfConstants.DEFAULT_LOGIN_FAILURE_ATTEMPTS_NUMBER);
        individualUserRepository.update(user, user.getInuId());
    }

    @Override
    public GryfIndUserDto saveNewVerificationCodeForIndividual(Long individualId, String verificationCode) {
        IndividualUser user = individualUserRepository.findByIndividualId(individualId);
        user.setVerificationCode(AEScryptographer.encrypt(verificationCode));
        return individualUserEntityMapper.convert(individualUserRepository.update(user, user.getInuId()));
    }

}
