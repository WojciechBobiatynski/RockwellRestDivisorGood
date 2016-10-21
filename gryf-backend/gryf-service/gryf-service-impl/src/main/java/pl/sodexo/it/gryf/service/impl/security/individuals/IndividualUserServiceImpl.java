package pl.sodexo.it.gryf.service.impl.security.individuals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.dao.api.crud.dao.individuals.IndividualUserDao;
import pl.sodexo.it.gryf.service.api.security.individuals.IndividualUserService;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.security.individuals.GryfIndUserDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.security.individuals.IndividualUserEntityMapper;

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

    @Override
    public GryfIndUserDto saveIndUser(GryfIndUserDto gryfIndUserDto) {
        return individualUserEntityMapper.convert(individualUserDao.save(gryfIndUserDtoMapper.convert(gryfIndUserDto)));
    }
}
