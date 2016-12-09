package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionAttachmentDto;
import pl.sodexo.it.gryf.dao.api.search.dao.CorrectionAttachmentSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.CorrectionAttachmentSearchMapper;

/**
 * Implementacja dao do operacji na erozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class CorrectionAttachmentSearchDaoImpl implements CorrectionAttachmentSearchDao {

    @Autowired
    private CorrectionAttachmentSearchMapper correctionAttachmentSearchMapper;

    @Override
    public CorrectionAttachmentDto getCorrAttByAttByErmbsAttIdAndCorrId(Long corrId, Long ermbsAttId) {
        return correctionAttachmentSearchMapper.getCorrAttByAttByErmbsAttIdAndCorrId(new UserCriteria(), corrId, ermbsAttId);
    }
}
