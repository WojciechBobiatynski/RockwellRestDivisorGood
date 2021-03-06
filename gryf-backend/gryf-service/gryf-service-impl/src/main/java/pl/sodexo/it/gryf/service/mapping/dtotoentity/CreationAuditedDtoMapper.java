package pl.sodexo.it.gryf.service.mapping.dtotoentity;

import pl.sodexo.it.gryf.common.dto.api.CreationAuditedDto;
import pl.sodexo.it.gryf.model.api.CreationAuditedEntity;

/**
 * Maper mapujacy CreationAuditedDto na encję CreationAuditedEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class CreationAuditedDtoMapper<Dto extends CreationAuditedDto, Entity extends CreationAuditedEntity> extends GryfDtoMapper<Dto, Entity> {

    @Override
    protected void map(Dto dto, Entity entity) {
        super.map(dto, entity);
        entity.setCreatedTimestamp(dto.getCreatedTimestamp());
        entity.setCreatedUser(dto.getCreatedUser());
    }
}
