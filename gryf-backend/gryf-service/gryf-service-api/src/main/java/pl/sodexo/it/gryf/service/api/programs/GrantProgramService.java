package pl.sodexo.it.gryf.service.api.programs;

import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;

/**
 * Usługa dla obiektu GrantProgram
 */
public interface GrantProgramService {
    /**
     * Wybiera probram po id
     *
     * @param grantProgramid Identyfikator programu - klucz wewnetrzny
     * @return Dto Programu
     */
    GrantProgramDictionaryDTO getGrantProgramById(Long grantProgramid);
}
