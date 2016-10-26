package pl.sodexo.it.gryf.service.mapping.entitytodto;

import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

/**
 * Mapper mapujący VersionableEntity na VersionableDto
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class VersionableEntityMapper<Entity extends VersionableEntity, Dto extends VersionableDto> extends AuditableEntityMapper<Entity, Dto> {

    @Override
    public void map(Entity entity, Dto dto) {
        super.map(entity, dto);
        dto.setVersion(entity.getVersion());
    }
}
