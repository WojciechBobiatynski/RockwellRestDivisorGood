package pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.service.mapping.GenericMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.StateEntityMapper;

/**
 * Mapper mapujący encję ZipCode na ZipCodeDto
 *
 * Created by jbentyn on 2016-09-23.
 */
@Component
public class ZipCodeEntityToSearchResultMapper extends GenericMapper<ZipCode, ZipCodeSearchResultDTO> {

    @Autowired
    private StateEntityMapper stateEntityMapper;

    @Override
    protected ZipCodeSearchResultDTO initDestination() {
        return new ZipCodeSearchResultDTO();
    }

    @Override
    public void map(ZipCode entity, ZipCodeSearchResultDTO dto) {
        dto.setId(entity.getId());
        dto.setStateName(entity.getState().getName());
        dto.setZipCode(entity.getZipCode());
        dto.setCityName(entity.getCityName());
        dto.setActive(entity.getActive());
    }

}
