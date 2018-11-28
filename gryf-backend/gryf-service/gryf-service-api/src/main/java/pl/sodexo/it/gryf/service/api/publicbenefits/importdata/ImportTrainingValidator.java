package pl.sodexo.it.gryf.service.api.publicbenefits.importdata;

import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportTrainingDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;

import java.util.List;

/**
 * Created by tburnicki on 2018-11-28
 */
public interface ImportTrainingValidator {

    String ENTITY_CONSTRAINT_VIOLATION_NOT_EXISTS_IN_PROGRAM = "Kategoria %s nie jest zdefiniowana w programie";

    List<EntityConstraintViolation> validate(GrantProgramDictionaryDTO grantProgram, ImportTrainingDTO importDTO);
}
