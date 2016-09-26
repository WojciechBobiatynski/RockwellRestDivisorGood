package pl.sodexo.it.gryf.service.mapping.dtoToEntity;

import pl.sodexo.it.gryf.common.dto.basic.GryfDto;
import pl.sodexo.it.gryf.model.GryfEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Maper mapujący GryfDto na encję GryfEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
public abstract class GryfDtoMapper<Dto extends GryfDto, Entity extends GryfEntity> {

    protected abstract Entity initEntity();

    protected void map(Dto dto, Entity entity){

    }

    public Entity convert(Dto dto) {
        Entity entity = initEntity();
        if (dto != null) {
            map(dto, entity);
        }
        return entity;
    }

    public List<Entity> convert( List<Dto> dtoList) {
        List<Entity> result = new ArrayList<>();
        dtoList.forEach(dto -> result.add(convert(dto)));
        return result;
    }
}
