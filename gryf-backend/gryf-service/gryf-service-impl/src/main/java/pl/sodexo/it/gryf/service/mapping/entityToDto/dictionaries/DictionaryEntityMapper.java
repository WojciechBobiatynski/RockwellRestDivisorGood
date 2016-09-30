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

    private void map(DictionaryEntity entity, DictionaryDTO dto) {
        dto.setId(entity.getDictionaryId());
        dto.setName(entity.getDictionaryName());
    }

    //TODO AdamK: na razie tak
    public List<DictionaryDTO> convert(List<? extends DictionaryEntity> sourceList) {
        List<DictionaryDTO> result = new ArrayList<>();
        if (sourceList != null) {
            sourceList.stream().map(o -> convert(o)).forEach(result::add);
        }
        return result;
    }

    public DictionaryDTO convert(DictionaryEntity source) {
        DictionaryDTO destination = new DictionaryDTO();
        if (source != null) {
            map(source, destination);
        }
        return destination;
    }
}
