package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionAttachmentDto;

/**
 * Mapper do operacji na załącznikach do korekt dla rozliczeń bonów elektronicznych
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface CorrectionAttachmentSearchMapper {

    /**
     * Pobiera id załącznika korekty względem id załącznika i id korekty
     * @param criteria - krytertia użytkownika
     * @param corrId - id rozliczenia
     * @param ermbsAttId - id rozliczenia
     * @return Dto rozliczenia
     */
    CorrectionAttachmentDto getCorrAttByAttByErmbsAttIdAndCorrId(@Param("criteria") UserCriteria criteria, @Param("corrId") Long corrId, @Param("ermbsAttId") Long ermbsAttId);

}
