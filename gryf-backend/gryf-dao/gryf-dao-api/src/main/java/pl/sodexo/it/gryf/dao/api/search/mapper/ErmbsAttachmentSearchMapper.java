package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;

import java.util.List;

/**
 * Mapper do operacji na załącznikach e-rozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface ErmbsAttachmentSearchMapper {

    /**
     * Zwraca Dto załączników rozliczenia na podstawie idków
     * @param criteria - kryteria użytkownika
     * @param attachmentsIds - identyfikatory rozliczenia
     * @return lista dto
     */
    List<ErmbsAttachmentDto> findErmbsAttachmentsByIds(@Param("criteria") UserCriteria criteria,@Param("attachmentsIds")  List<Long> attachmentsIds);

}
