package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramAttachmentTypeDto;

import java.util.List;

/**
 * Mapper dla załączników
 *
 * Created by akmiecinski on 23.11.2016.
 */
public interface AttachmentSearchMapper {

    /**
     * Metoda znajdująca typy załączników dla programu dofinansowania wraz z falgą czy wymagane
     * @param criteria - kryteria użytkownika
     * @param grantProgramId - id programu dofiannsowania
     * @return lista typów załączników
     */
    List<GrantProgramAttachmentTypeDto> findAttachmentTypesByGrantProgramId(@Param("criteria") UserCriteria criteria, @Param("grantProgramId") Long grantProgramId);

}
