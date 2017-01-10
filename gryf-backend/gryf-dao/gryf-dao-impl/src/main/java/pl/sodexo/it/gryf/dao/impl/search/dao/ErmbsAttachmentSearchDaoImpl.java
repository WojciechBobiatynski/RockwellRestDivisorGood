package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.dao.api.search.dao.ErmbsAttachmentSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.ErmbsAttachmentSearchMapper;

import java.util.List;

/**
 * Implementacja dao do operacji na erozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ErmbsAttachmentSearchDaoImpl implements ErmbsAttachmentSearchDao {

    @Autowired
    private ErmbsAttachmentSearchMapper ermbsAttachmentSearchMapper;

    @Override
    public List<ErmbsAttachmentDto> findErmbsAttachmentsByIds(List<Long> attachmentsIds) {
        return ermbsAttachmentSearchMapper.findErmbsAttachmentsByIds(new UserCriteria(), attachmentsIds);
    }
}
