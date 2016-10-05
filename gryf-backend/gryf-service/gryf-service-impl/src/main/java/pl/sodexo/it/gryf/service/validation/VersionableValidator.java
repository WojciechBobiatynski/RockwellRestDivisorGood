package pl.sodexo.it.gryf.service.validation;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import java.util.Objects;

/**
 * Walidator wersji encji
 *
 * Created by jbentyn on 2016-09-30.
 */
@Component
public class VersionableValidator {

    public void validateVersion(VersionableEntity entity, Integer version, Long id) {
        if (!Objects.equals(entity.getVersion(), version)) {
            throw new StaleDataException(id, entity);
        }
    }

    public void validateVersion(VersionableEntity entity, VersionableDto dto, Long id) {
        if (!Objects.equals(entity.getVersion(), dto.getVersion())) {
            throw new StaleDataException(id, entity);
        }
    }

}
