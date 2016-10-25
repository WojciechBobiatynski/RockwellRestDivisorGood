package pl.sodexo.it.gryf.service.impl.publicbenefits.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantprograms.GrantProgramRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.service.api.publicbenefits.contracts.ContractService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;

import java.util.ArrayList;
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
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Override
    public List<GrantProgramDictionaryDTO> FindGrantProgramsDictionaries() {
        List<GrantProgram> grantPrograms = grantProgramRepository.findProgramsByDate(new Date());
        return convert(grantPrograms);
    }

    private static List<GrantProgramDictionaryDTO> convert(List<GrantProgram> grantPrograms) {
        List<GrantProgramDictionaryDTO> dtos = new ArrayList<>();
        for (GrantProgram grantProgram : grantPrograms) {
            GrantProgramDictionaryDTO dto = new GrantProgramDictionaryDTO();
            dto.setId(grantProgram.getId());
            dto.setName(grantProgram.getProgramName());
            dto.setGrantProgramOwnerName(grantProgram.getGrantOwner().getName());
            dtos.add(dto);
        }
        return dtos;
    }
}
