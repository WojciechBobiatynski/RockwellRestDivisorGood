package pl.sodexo.it.gryf.service.api.other;

import pl.sodexo.it.gryf.common.dto.other.GrantProgramAttachmentTypeDto;

import java.util.List;

/**
 * Serwis dla operacji na załącznikach
 *
 * Created by akmiecinski on 23.11.2016.
 */
public interface AttachmentService {

    /**
     * Metoda znajdująca typy załączników dla programu dofinansowania wraz z falgą czy wymagane
     * @param grantProgramId - id programu dofiannsowania
     * @return lista typów załączników
     */
    List<GrantProgramAttachmentTypeDto> findAttachmentTypesByGrantProgramId(Long grantProgramId);

}
