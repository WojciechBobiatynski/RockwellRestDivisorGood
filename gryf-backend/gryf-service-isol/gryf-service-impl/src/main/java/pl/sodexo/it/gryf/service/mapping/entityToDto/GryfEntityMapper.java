package pl.sodexo.it.gryf.service.mapping.entityToDto;

import pl.sodexo.it.gryf.common.dto.basic.GryfDto;
import pl.sodexo.it.gryf.model.GryfEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper mapujący encję GryfEntity na GryfDto
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class GryfEntityMapper<Entity extends GryfEntity, Dto extends GryfDto> {

    protected abstract Dto initDto();

    protected void map(Entity entity, Dto dto){

    }

    public Dto convert(Entity entity) {
        Dto dto = initDto();
        if (entity != null) {
            map(entity, dto);
        }
        return dto;
    }

    public List<Dto> convert(List<Entity> enities) {
        List<Dto> result = new ArrayList<>();
        for (Entity entity : enities) {
            result.add(convert(entity));
        }
        return result;
    }
}
