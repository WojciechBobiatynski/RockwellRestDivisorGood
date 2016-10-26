package pl.sodexo.it.gryf.service.mapping.dtotoentity.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.VersionableDtoMapper;

/**
 * Maper mapujący dto ZipCodeDto na encję ZipCode
 *
 * Created by jbentyn on 2016-09-23.
 */
@Component
public class ZipCodeDtoMapper extends VersionableDtoMapper<ZipCodeDto, ZipCode> {

    @Autowired
    private StateDtoMapper stateDtoMapper;

    @Override
    protected ZipCode initDestination() {
        return new ZipCode();
    }

    @Override
    protected void map( ZipCodeDto dto, ZipCode entity) {
        super.map(dto, entity);
        entity.setId(dto.getId());
        entity.setState(stateDtoMapper.convert(dto.getState()));
        entity.setZipCode(dto.getZipCode());
        entity.setCityName(dto.getCityName());
        entity.setActive(dto.getActive());
    }
}
