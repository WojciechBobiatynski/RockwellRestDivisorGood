package pl.sodexo.it.gryf.service.mapping.dtoToEntity.dictionaries;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.StateDto;
import pl.sodexo.it.gryf.model.dictionaries.State;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.GryfDtoMapper;

/**
 * Maper mapujący dto StateDto na encję State
 *
 * Created by jbentyn on 2016-09-23.
 */
@Component
public class StateDtoMapper extends GryfDtoMapper<StateDto, State> {

    @Override
    protected State initDestination() {
        return new State();
    }

    @Override
    protected void map(StateDto dto, State entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCountry(dto.getCountry());
    }
}
