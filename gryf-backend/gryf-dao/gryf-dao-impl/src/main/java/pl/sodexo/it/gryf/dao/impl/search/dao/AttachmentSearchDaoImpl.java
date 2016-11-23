package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramAttachmentTypeDto;
import pl.sodexo.it.gryf.dao.api.search.dao.AttachmentSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.AttachmentSearchMapper;

import java.util.List;

/**
 * Dao dla operacji na załącznikach
 *
 * Created by akmiecinski on 23.11.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AttachmentSearchDaoImpl implements AttachmentSearchDao {

    @Autowired
    private AttachmentSearchMapper attachmentSearchMapper;

    @Override
    public List<GrantProgramAttachmentTypeDto> findAttachmentTypesByGrantProgramId(Long grantProgramId) {
        return attachmentSearchMapper.findAttachmentTypesByGrantProgramId(new UserCriteria(), grantProgramId);
    }
}
