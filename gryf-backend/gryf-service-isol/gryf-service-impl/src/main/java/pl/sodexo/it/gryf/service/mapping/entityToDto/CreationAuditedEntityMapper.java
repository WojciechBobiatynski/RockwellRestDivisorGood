package pl.sodexo.it.gryf.service.mapping.entityToDto;

import pl.sodexo.it.gryf.common.dto.basic.CreationAuditedDto;
import pl.sodexo.it.gryf.model.CreationAuditedEntity;

/**
 * Mapper mapujący CreationAuditedEntity na CreationAuditedDto
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class CreationAuditedEntityMapper<Entity extends CreationAuditedEntity, Dto extends CreationAuditedDto> extends GryfEntityMapper<Entity, Dto> {

    @Override
    public void map(Entity entity, Dto dto) {
        super.map(entity, dto);
        dto.setCreatedTimestamp(entity.getCreatedTimestamp());
        dto.setCreatedUser(entity.getCreatedUser());
    }
}
