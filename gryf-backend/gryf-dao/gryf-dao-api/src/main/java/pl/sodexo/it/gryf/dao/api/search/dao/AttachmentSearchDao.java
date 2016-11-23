package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.other.GrantProgramAttachmentTypeDto;

import java.util.List;

/**
 * Dao dla operacji na załącznikach
 *
 * Created by akmiecinski on 23.11.2016.
 */
public interface AttachmentSearchDao {

    /**
     * Metoda znajdująca typy załączników dla programu dofinansowania wraz z falgą czy wymagane
     * @param grantProgramId - id programu dofiannsowania
     * @return lista typów załączników
     */
    List<GrantProgramAttachmentTypeDto> findAttachmentTypesByGrantProgramId(Long grantProgramId);

}
