package pl.sodexo.it.gryf.service.impl.other;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramAttachmentTypeDto;
import pl.sodexo.it.gryf.dao.api.search.dao.AttachmentSearchDao;
import pl.sodexo.it.gryf.service.api.other.AttachmentService;

import java.util.List;

/**
 * Implementacja serwisu do operacji na załącznikach
 *
 * Created by akmiecinski on 23.11.2016.
 */
@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService{

    @Autowired
    private AttachmentSearchDao attachmentSearchDao;

    @Override
    public List<GrantProgramAttachmentTypeDto> findAttachmentTypesByGrantProgramId(Long grantProgramId) {
        return attachmentSearchDao.findAttachmentTypesByGrantProgramId(grantProgramId);
    }
}
