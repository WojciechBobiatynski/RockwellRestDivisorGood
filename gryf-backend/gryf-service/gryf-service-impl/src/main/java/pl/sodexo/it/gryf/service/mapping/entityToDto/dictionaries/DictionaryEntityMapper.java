package pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.model.DictionaryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class DictionaryEntityMapper {

    private DictionaryDTO initDestination() {
        return new DictionaryDTO();
    }

    private void map(DictionaryEntity entity, DictionaryDTO dto) {
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

    public DictionaryDTO convert(DictionaryEntity source) {
        DictionaryDTO destination = initDestination();
        if (source != null) {
            map(source, destination);
        }
        return destination;
    }
}
