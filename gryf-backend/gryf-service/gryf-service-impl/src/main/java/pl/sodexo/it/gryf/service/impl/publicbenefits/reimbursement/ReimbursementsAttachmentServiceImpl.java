package pl.sodexo.it.gryf.service.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementAttachmentRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementTraineeAttachmentRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachment;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementsAttachmentService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.searchform.ReimbursementEntityToSearchResultMapper;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-09.
 */
@Service
@Transactional
public class ReimbursementsAttachmentServiceImpl implements ReimbursementsAttachmentService {

    //FIELDS

    @Autowired
    private FileService fileService;

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    @Autowired
    private ReimbursementAttachmentRepository reimbursementAttachmentRepository;

    @Autowired
    private ReimbursementTraineeAttachmentRepository reimbursementTraineeAttachmentRepository;

    @Autowired
    private ReimbursementEntityToSearchResultMapper reimbursementEntityToSearchResultMapper;

    //PUBLIC METHODS

    @Override
    public FileDTO getReimbursementTraineeAttachmentFile(Long attachmentId) {
        ReimbursementTraineeAttachment attachment = reimbursementTraineeAttachmentRepository.get(attachmentId);
        FileDTO dto = new FileDTO();
        dto.setName(StringUtils.findFileNameInPath(attachment.getFileLocation()));
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }

    @Override
    public List<ReimbursementSearchResultDTO> findReimbursementsSearchResults(ReimbursementSearchQueryDTO searchDTO) {
        List<Reimbursement> reimbursements = reimbursementRepository.findReimbursements(searchDTO);
        return reimbursementEntityToSearchResultMapper.convert(reimbursements);
    }

    @Override
    public FileDTO getReimbursementAttachmentFile(Long attachmentId) {
        ReimbursementAttachment attachment = reimbursementAttachmentRepository.get(attachmentId);
        FileDTO dto = new FileDTO();
        dto.setName(StringUtils.findFileNameInPath(attachment.getFileLocation()));
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }

}
