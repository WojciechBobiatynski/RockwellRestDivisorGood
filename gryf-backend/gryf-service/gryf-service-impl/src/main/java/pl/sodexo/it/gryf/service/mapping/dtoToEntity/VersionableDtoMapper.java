package pl.sodexo.it.gryf.service.mapping.dtoToEntity;

import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

/**
 * Maper mapujący VersionableDto na encję VersionableEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class VersionableDtoMapper< Dto extends VersionableDto,Entity extends VersionableEntity> extends AuditableDtoMapper<Dto, Entity> {

    @Override
    protected void map(Dto dto, Entity entity) {
        super.map(dto, entity);
        entity.setVersion(dto.getVersion());
    }
}
