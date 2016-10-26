package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.grantprograms;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.service.mapping.entitytodto.GryfEntityMapper;

/**
 * Created by adziobek on 25.10.2016.
 */
@Component
public class GrantProgramEntityMapper extends GryfEntityMapper<GrantProgram, GrantProgramDictionaryDTO> {

    @Override
    protected GrantProgramDictionaryDTO initDestination() {return new GrantProgramDictionaryDTO();}

    @Override
    public void map(GrantProgram entity, GrantProgramDictionaryDTO dto) {
        dto.setId(entity.getId());
        dto.setName(entity.getProgramName());
        dto.setGrantProgramOwnerName(entity.getGrantOwner().getName());
    }
}
