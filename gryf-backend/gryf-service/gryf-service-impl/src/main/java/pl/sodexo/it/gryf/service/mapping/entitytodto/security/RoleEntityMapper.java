package pl.sodexo.it.gryf.service.mapping.entitytodto.security;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.model.security.TeRole;
import pl.sodexo.it.gryf.service.mapping.entitytodto.GryfEntityMapper;

/**
 * Komponent mapujący rolę na dto
 *
 * Created by akmiecinski on 08.11.2016.
 */
@Component
public class RoleEntityMapper extends GryfEntityMapper<TeRole, RoleDto> {

    @Override
    protected RoleDto initDestination() {
        return new RoleDto();
    }

    @Override
    protected void map(TeRole entity, RoleDto dto) {
        super.map(entity, dto);
        dto.setCode(entity.getCode());
        dto.setContext(entity.getContext());
        dto.setDescription(entity.getDescription());
    }
}
