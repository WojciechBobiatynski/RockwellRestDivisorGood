package pl.sodexo.it.gryf.service.impl.publicbenefits.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.grantprograms.GrantProgramEntityMapper;

import java.util.Date;
import java.util.List;

/**
 * Created by adziobek on 25.10.2016.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    @Autowired
    private GrantProgramRepository grantProgramRepository;

    @Autowired
    private GrantProgramEntityMapper grantProgramEntityMapper;

    @Override
    public List<GrantProgramDictionaryDTO> FindGrantProgramsDictionaries() {
        List<GrantProgram> grantPrograms = grantProgramRepository.findProgramsByDate(new Date());
        return grantProgramEntityMapper.convert(grantPrograms);
    }
}
