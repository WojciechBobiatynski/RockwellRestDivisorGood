package pl.sodexo.it.gryf.service.impl.programs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.service.api.programs.GrantProgramService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.grantprograms.GrantProgramEntityMapper;

@Service
@Transactional
public class GrantProgramServiceImpl implements GrantProgramService {

    @Autowired
    private GrantProgramRepository grantProgramRepository;

    @Autowired
    private GrantProgramEntityMapper grantProgramEntityMapper;

    @Override
    public GrantProgramDictionaryDTO getGrantProgramById(Long grantProgramid) {
        return grantProgramEntityMapper.convert(grantProgramRepository.get(grantProgramid));
    }
}
