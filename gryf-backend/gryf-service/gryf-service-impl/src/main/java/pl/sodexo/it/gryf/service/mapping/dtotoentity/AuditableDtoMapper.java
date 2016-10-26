package pl.sodexo.it.gryf.service.mapping.dtotoentity;

import pl.sodexo.it.gryf.common.dto.api.AuditableDto;
import pl.sodexo.it.gryf.model.api.AuditableEntity;

/**
 * Maper mapujący AuditableDto na encję AuditableEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class AuditableDtoMapper<Dto extends AuditableDto, Entity extends AuditableEntity> extends CreationAuditedDtoMapper<Dto, Entity> {

    @Override
    protected void map(Dto dto, Entity entity) {
        super.map(dto, entity);
        entity.setModifiedTimestamp(dto.getModifiedTimestamp());
        entity.setModifiedUser(dto.getModifiedUser());
    }
}
