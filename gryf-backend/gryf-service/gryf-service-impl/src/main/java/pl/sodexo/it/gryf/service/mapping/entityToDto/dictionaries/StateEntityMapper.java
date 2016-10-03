package pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.StateDto;
import pl.sodexo.it.gryf.model.dictionaries.State;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;

/**
 * Mapper mapujący encje State na StateDto
 *
 * Created by jbentyn on 2016-09-23.
 */
@Component
public class StateEntityMapper extends GryfEntityMapper<State, StateDto> {

    @Override
    protected StateDto initDestination() {
        return new StateDto();
    }

    @Override
    public void map(State entity, StateDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(entity.getCountry());
    }
}
