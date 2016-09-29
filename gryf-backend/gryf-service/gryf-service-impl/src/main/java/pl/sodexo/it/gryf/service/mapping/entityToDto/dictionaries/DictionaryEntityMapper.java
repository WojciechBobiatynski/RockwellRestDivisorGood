package pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchResultDTO;
import pl.sodexo.it.gryf.model.DictionaryEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;
import pl.sodexo.it.gryf.service.mapping.GenericMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class DictionaryEntityMapper extends GenericMapper<DictionaryEntity, DictionaryDTO> {

    @Override
    protected DictionaryDTO initDestination() {
        return new DictionaryDTO();
    }

    @Override
    public void map(DictionaryEntity entity, DictionaryDTO dto) {
        dto.setId(entity.getDictionaryId());
        dto.setName(entity.getDictionaryName());
    }

    public List<DictionaryDTO> convert(List<? extends DictionaryEntity> sourceList) {
        List<DictionaryDTO> result = new ArrayList<>();
        if (sourceList != null) {
            for(DictionaryEntity source:sourceList){
                result.add(convert(source));
            }
        }
        return result;
    }
}
