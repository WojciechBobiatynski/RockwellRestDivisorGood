package pl.sodexo.it.gryf.service.mapping.entityToDto;

import pl.sodexo.it.gryf.common.dto.api.AuditableDto;
import pl.sodexo.it.gryf.model.api.AuditableEntity;

/**
 * Mapper mapujący AuditableEntity na AuditableDto
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class AuditableEntityMapper<Entity extends AuditableEntity, Dto extends AuditableDto> extends CreationAuditedEntityMapper<Entity, Dto> {

    @Override
    public void map(Entity entity, Dto dto) {
        super.map(entity, dto);
        dto.setModifiedTimestamp(entity.getModifiedTimestamp());
        dto.setModifiedUser(entity.getModifiedUser());
    }
}
