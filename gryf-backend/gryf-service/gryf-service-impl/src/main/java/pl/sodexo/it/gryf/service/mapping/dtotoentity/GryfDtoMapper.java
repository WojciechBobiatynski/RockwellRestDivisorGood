package pl.sodexo.it.gryf.service.mapping.dtotoentity;

import pl.sodexo.it.gryf.common.dto.api.GryfDto;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.service.mapping.GenericMapper;

/**
 * Maper mapujący GryfDto na encję GryfEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class GryfDtoMapper<Dto extends GryfDto, Entity extends GryfEntity> extends GenericMapper<Dto, Entity> {

    @Override
    protected void map(Dto dto, Entity entity) {

    }
}
