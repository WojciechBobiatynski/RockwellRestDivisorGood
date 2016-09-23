package pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.service.mapping.entityToDto.VersionableEntityMapper;

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
    protected ZipCodeDto initDto() {
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
