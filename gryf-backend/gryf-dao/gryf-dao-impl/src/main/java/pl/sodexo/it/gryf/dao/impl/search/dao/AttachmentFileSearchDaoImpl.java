package pl.sodexo.it.gryf.dao.impl.search.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.dto.attachments.AttachmentFileDto;
import pl.sodexo.it.gryf.dao.api.search.dao.AttachmentFileSearchDao;
import pl.sodexo.it.gryf.dao.api.search.mapper.AttachmentFileSearchMapper;

import java.util.List;

/**
 * Dao dla plików załączników
 *
 * Created by akmiecinski on 23.11.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AttachmentFileSearchDaoImpl implements AttachmentFileSearchDao{

    @Autowired
    private AttachmentFileSearchMapper attachmentFileSearchMapper;

    @Override
    public List<AttachmentFileDto> findAttachmentsToDelete() {
        return attachmentFileSearchMapper.findAttachmentsToDelete(new UserCriteria());
    }
}
