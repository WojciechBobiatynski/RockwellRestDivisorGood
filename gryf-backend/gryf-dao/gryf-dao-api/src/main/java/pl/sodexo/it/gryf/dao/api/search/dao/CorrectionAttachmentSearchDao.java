package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionAttachmentDto;

/**
 * Dao dla operacji korektach dla rozliczeń
 *
 * Created by akmiecinski on 23.11.2016.
 */
public interface CorrectionAttachmentSearchDao {

    /**
     * Pobiera id załącznika korekty względem id załącznika i id korekty
     * @param corrId - id rozliczenia
     * @param ermbsAttId - id rozliczenia
     * @return Dto rozliczenia
     */
    CorrectionAttachmentDto getCorrAttByAttByErmbsAttIdAndCorrId(Long corrId, Long ermbsAttId);
}
