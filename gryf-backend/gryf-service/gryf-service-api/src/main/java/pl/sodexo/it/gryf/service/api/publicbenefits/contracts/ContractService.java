package pl.sodexo.it.gryf.service.api.publicbenefits.contracts;

import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;

import java.util.List;

/**
 * Created by adziobek on 25.10.2016.
 */
public interface ContractService {
    List<GrantProgramDictionaryDTO> FindGrantProgramsDictionaries();
}