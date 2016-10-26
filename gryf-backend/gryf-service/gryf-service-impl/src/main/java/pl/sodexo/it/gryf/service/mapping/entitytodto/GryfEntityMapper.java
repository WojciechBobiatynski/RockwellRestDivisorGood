package pl.sodexo.it.gryf.service.mapping.entitytodto;

import pl.sodexo.it.gryf.common.dto.api.GryfDto;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.service.mapping.GenericMapper;

/**
 * Mapper mapujący encję GryfEntity na GryfDto
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class GryfEntityMapper<Entity extends GryfEntity, Dto extends GryfDto> extends GenericMapper<Entity,Dto> {

    @Override
    protected void map(Entity entity, Dto dto) {

    }
}
