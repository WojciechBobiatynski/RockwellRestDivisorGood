package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;

import java.util.List;

/**
 * Dao dla operacji na erozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface ErmbsAttachmentSearchDao {

    /**
     * Zwraca Dto załączników rozliczenia na podstawie idków
     * @param attachmentsIds - identyfikatory rozliczenia
     * @return lista dto
     */
    List<ErmbsAttachmentDto> findErmbsAttachmentsByIds(List<Long> attachmentsIds);

}
