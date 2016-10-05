package pl.sodexo.it.gryf.service.local.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementAttachmentDTO;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.YesNo;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementAttachmentRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementAttachmentTypeRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.*;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement.ReimbursementAttachmentLocalService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jbentyn on 2016-09-30.
 */
@Service
public class ReimbursementAttachmentLocalServiceImpl implements ReimbursementAttachmentLocalService {

    @Autowired
    private FileService fileService;

    @Autowired
    private ReimbursementAttachmentRepository reimbursementAttachmentRepository;

    @Autowired
    private ReimbursementAttachmentTypeRepository reimbursementAttachmentTypeRepository;

    @Autowired
    private GryfValidator gryfValidator;

    @Override
    public String findAttachmentName(Reimbursement reimbursement, String attachmentType, Long attachmentId, String attachmentName) {
        ReimbursementDelivery reimbursementDelivery = reimbursement.getReimbursementDelivery();
        String fileName = String.format("%s_%s_%s_%s_%s", reimbursementDelivery.getId(), reimbursement.getId(), attachmentType, attachmentId, attachmentName);
        return StringUtils.convertFileName(fileName);
    }

    private boolean isAttachmentRequired(ReimbursementDelivery delivery, String name) {
        ReimbursementPattern pattern = delivery.getReimbursementPattern();
        for (ReimbursementAttachmentRequired attachmentRequired : pattern.getReimbursementAttachmentRequiredList()) {
            if (Objects.equals(attachmentRequired.getName(), name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void organizeAttachmentList(Reimbursement reimbursement, List<ReimbursementAttachmentDTO> attachmentDtoList, boolean isRemoveAllowed) {
        if (attachmentDtoList == null) {
            attachmentDtoList = new ArrayList<>();
        }
        List<ReimbursementAttachmentTempDTO> attachmentTempDtoList = makeAttachmentTempDtoList(reimbursement.getReimbursementAttachments(), attachmentDtoList);

        for (ReimbursementAttachmentTempDTO attachmentTempDTO : attachmentTempDtoList) {
            ReimbursementAttachment attachment = attachmentTempDTO.getAttachment();
            ReimbursementAttachmentDTO attachmentDTO = attachmentTempDTO.getAttachmentDTO();

            //UPDATE OLD RECORD
            if (attachment != null && attachmentDTO != null) {
                fillAttachment(reimbursement, attachment, attachmentDTO);
                saveAttachment(reimbursement, attachment, attachmentDTO);

                //REMOVE OLD RECORD
            } else if (attachment != null) {
                if (isRemoveAllowed) {
                    reimbursement.removeReimbursementAttachment(attachment);
                    fileService.deleteFile(attachment.getFileLocation());
                } else {
                    gryfValidator.validate(String.format("Nie jest możliwe usunięcie załącznika o nazwie '%s", attachment.getName()));
                }

                //ADD NEW RECORD
            } else if (attachmentDTO != null) {
                attachment = reimbursementAttachmentRepository.save(new ReimbursementAttachment());
                fillAttachment(reimbursement, attachment, attachmentDTO);
                saveAttachment(reimbursement, attachment, attachmentDTO);
            }
        }
    }

    private void saveAttachment(Reimbursement reimbursement, ReimbursementAttachment attachment, ReimbursementAttachmentDTO attachmentDTO) {
        if (attachmentDTO.getFile() != null) {
            FileDTO fileDTO = attachmentDTO.getFile();
            String fileName = findAttachmentName(reimbursement, ReimbursementAttachment.ATTACHMENT_TYPE_IN_PATH, attachment.getId(), attachment.getName());
            String fileLocation = fileService.writeFile(FileType.REIMBURSEMENTS, fileName, fileDTO, attachment);
            if (attachment.getFileLocation() != null && !fileLocation.equals(attachment.getFileLocation())) {
                fileService.deleteFile(attachment.getFileLocation());
            }
            attachment.setFileLocation(fileLocation);
            attachment.setOriginalFileName(fileDTO.getOriginalFilename());
            attachmentDTO.setOriginalFileName(fileDTO.getOriginalFilename());
            attachmentDTO.setFileIncluded(false);
        }
    }

    private void fillAttachment(Reimbursement reimbursement, ReimbursementAttachment attachment, ReimbursementAttachmentDTO attachmentDTO) {
        attachment.setName(attachmentDTO.getName());
        attachment.setAttachmentType(attachmentDTO.getAttachmentType() != null ? reimbursementAttachmentTypeRepository.get((String) attachmentDTO.getAttachmentType().getId()) : null);
        attachment.setRemarks(attachmentDTO.getRemarks());
        attachment.setRequired(YesNo.fromBoolean(isAttachmentRequired(reimbursement.getReimbursementDelivery(), attachment.getName())));

        reimbursement.addReimbursementAttachment(attachment);
    }

    private List<ReimbursementAttachmentTempDTO> makeAttachmentTempDtoList(List<ReimbursementAttachment> attachments, List<ReimbursementAttachmentDTO> attachmentDtoList) {
        List<ReimbursementAttachmentTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, ReimbursementAttachment> attachmentMap = GryfUtils.constructMap(attachments, new GryfUtils.MapConstructor<Long, ReimbursementAttachment>() {

            public boolean isAddToMap(ReimbursementAttachment input) {
                return input.getId() != null;
            }

            public Long getKey(ReimbursementAttachment input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, ReimbursementAttachmentDTO> attachmentDtoMap = GryfUtils.constructMap(attachmentDtoList, new GryfUtils.MapConstructor<Long, ReimbursementAttachmentDTO>() {

            public boolean isAddToMap(ReimbursementAttachmentDTO input) {
                return input.getId() != null;
            }

            public Long getKey(ReimbursementAttachmentDTO input) {
                return input.getId();
            }
        });

        //MAKE LIST
        for (ReimbursementAttachmentDTO attachmentDTO : attachmentDtoList) {
            ReimbursementAttachmentTempDTO temp = new ReimbursementAttachmentTempDTO();
            temp.setAttachmentDTO(attachmentDTO);
            temp.setAttachment((attachmentDTO.getId() != null) ? attachmentMap.get(attachmentDTO.getId()) : null);
            result.add(temp);
        }
        for (ReimbursementAttachment attachment : attachments) {
            if (!attachmentDtoMap.containsKey(attachment.getId())) {
                ReimbursementAttachmentTempDTO temp = new ReimbursementAttachmentTempDTO();
                temp.setAttachment(attachment);
                result.add(temp);
            }
        }

        return result;
    }

}
