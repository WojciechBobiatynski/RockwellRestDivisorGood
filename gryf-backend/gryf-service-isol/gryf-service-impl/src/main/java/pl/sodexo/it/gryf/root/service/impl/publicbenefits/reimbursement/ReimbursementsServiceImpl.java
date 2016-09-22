package pl.sodexo.it.gryf.root.service.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchResultDTO;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.*;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachment;
import pl.sodexo.it.gryf.root.service.local.FileService;
import pl.sodexo.it.gryf.root.service.publicbenefits.reimbursement.ReimbursementsService;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-09.
 */
@Service
@Transactional
public class ReimbursementsServiceImpl implements ReimbursementsService {

    //FIELDS

    @Autowired
    private FileService fileService;

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    @Autowired
    private ReimbursementAttachmentRepository reimbursementAttachmentRepository;

    @Autowired
    private ReimbursementTraineeAttachmentRepository reimbursementTraineeAttachmentRepository;

    //PUBLIC METHODS

    @Override
    public List<ReimbursementSearchResultDTO> findReimbursements(ReimbursementSearchQueryDTO searchDTO) {
        List<Reimbursement> reimbursements = reimbursementRepository.findReimbursements(searchDTO);
        return ReimbursementSearchResultDTO.createList(reimbursements);
    }

    @Override
    public FileDTO getReimbursementAttachmentFile(Long attachmentId) {
        ReimbursementAttachment attachment = reimbursementAttachmentRepository.get(attachmentId);
        FileDTO dto = new FileDTO();
        dto.setName(StringUtils.findFileNameInPath(attachment.getFileLocation()));
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }

    @Override
    public FileDTO getReimbursementTraineeAttachmentFile(Long attachmentId) {
        ReimbursementTraineeAttachment attachment = reimbursementTraineeAttachmentRepository.get(attachmentId);
        FileDTO dto = new FileDTO();
        dto.setName(StringUtils.findFileNameInPath(attachment.getFileLocation()));
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }
}
