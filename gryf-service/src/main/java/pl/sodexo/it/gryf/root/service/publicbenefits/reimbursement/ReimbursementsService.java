package pl.sodexo.it.gryf.root.service.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dto.FileDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchQueryDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachment;
import pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement.ReimbursementAttachmentRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement.ReimbursementRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.reimbursement.ReimbursementTraineeAttachmentRepository;
import pl.sodexo.it.gryf.root.service.FileService;
import pl.sodexo.it.gryf.utils.StringUtils;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-09.
 */
@Service
@Transactional
public class ReimbursementsService {

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

    public List<ReimbursementSearchResultDTO> findReimbursements(ReimbursementSearchQueryDTO searchDTO) {
        List<Reimbursement> reimbursements = reimbursementRepository.findReimbursements(searchDTO);
        return ReimbursementSearchResultDTO.createList(reimbursements);
    }

    public FileDTO getReimbursementAttachmentFile(Long attachmentId) {
        ReimbursementAttachment attachment = reimbursementAttachmentRepository.get(attachmentId);
        FileDTO dto = new FileDTO();
        dto.setName(StringUtils.findFileNameInPath(attachment.getFileLocation()));
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }

    public FileDTO getReimbursementTraineeAttachmentFile(Long attachmentId) {
        ReimbursementTraineeAttachment attachment = reimbursementTraineeAttachmentRepository.get(attachmentId);
        FileDTO dto = new FileDTO();
        dto.setName(StringUtils.findFileNameInPath(attachment.getFileLocation()));
        dto.setInputStream(fileService.getInputStream(attachment.getFileLocation()));
        return dto;
    }
}
