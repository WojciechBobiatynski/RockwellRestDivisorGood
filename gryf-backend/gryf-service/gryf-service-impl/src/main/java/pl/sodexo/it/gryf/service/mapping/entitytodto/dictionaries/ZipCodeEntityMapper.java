package pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.service.mapping.entitytodto.VersionableEntityMapper;

/**
 * Mapper mapujący encję ZipCode na ZipCodeDto
 *
 * Created by jbentyn on 2016-09-23.
 */
@Component
public class ZipCodeEntityMapper extends VersionableEntityMapper<ZipCode, ZipCodeDto> {

    @Autowired
    private StateEntityMapper stateEntityMapper;

    @Override
    protected ZipCodeDto initDestination() {
        return new ZipCodeDto();
    }

    @Override
    public void map(ZipCode entity, ZipCodeDto dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setState(stateEntityMapper.convert(entity.getState()));
        dto.setZipCode(entity.getZipCode());
        dto.setCityName(entity.getCityName());
        dto.setActive(entity.getActive());
    }

}
