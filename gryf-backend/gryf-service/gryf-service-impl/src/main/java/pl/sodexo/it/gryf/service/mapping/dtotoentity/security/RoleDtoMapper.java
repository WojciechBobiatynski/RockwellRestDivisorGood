package pl.sodexo.it.gryf.service.mapping.dtotoentity.security;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.model.security.TeRole;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.GryfDtoMapper;

/**
 * Komponent mapujący Dto roli na encję
 *
 * Created by akmiecinski on 08.11.2016.
 */
@Component
public class RoleDtoMapper extends GryfDtoMapper<RoleDto, TeRole> {

    @Override
    protected TeRole initDestination() {
        return new TeRole();
    }

    @Override
    public void map(RoleDto dto, TeRole entity){
        super.map(dto, entity);
        entity.setContext(dto.getContext());
        entity.setDescription(dto.getDescription());
        entity.setCode(dto.getCode());
    }
}
