package pl.sodexo.it.gryf.service.local.impl.publicbenefits.reimbursement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementTraineeAttachmentDTO;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.YesNo;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementAttachmentTypeRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement.ReimbursementTraineeAttachmentRepository;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.*;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement.ReimbursementAttachmentLocalService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.reimbursement.ReimbursementTraineeAttachmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jbentyn on 2016-09-30.
 */
@Service
public class ReimbursementTraineeAttachmentServiceImpl implements ReimbursementTraineeAttachmentService {

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private FileService fileService;

    @Autowired
    private ReimbursementTraineeAttachmentRepository reimbursementTraineeAttachmentRepository;

    @Autowired
    private ReimbursementAttachmentTypeRepository reimbursementAttachmentTypeRepository;

    @Autowired
    private ReimbursementAttachmentLocalService reimbursementAttachmentLocalService;

    @Override
    public void organizeTraineeAttachmentList(ReimbursementTrainee trainee, List<ReimbursementTraineeAttachmentDTO> traineeAttachmentDtoList, boolean isRemoveAllowed) {
        if (traineeAttachmentDtoList == null) {
            traineeAttachmentDtoList = new ArrayList<>();
        }
        List<ReimbursementTraineeAttachmentTempDTO> traineeAttachmentTempDtoList = makeTraineeAttachmentTempDtoList(trainee.getReimbursementTraineeAttachments(), traineeAttachmentDtoList);

        for (ReimbursementTraineeAttachmentTempDTO traineeAttachmentTempDTO : traineeAttachmentTempDtoList) {
            ReimbursementTraineeAttachment traineeAttachment = traineeAttachmentTempDTO.getTraineeAttachment();
            ReimbursementTraineeAttachmentDTO traineeAttachmentDTO = traineeAttachmentTempDTO.getTraineeAttachmentDTO();

            //UPDATE OLD RECORD
            if (traineeAttachment != null && traineeAttachmentDTO != null) {
                fillTraineeAttachment(trainee, traineeAttachment, traineeAttachmentDTO);
                saveTraineeAttachment(trainee, traineeAttachment, traineeAttachmentDTO);

                //REMOVE OLD RECORD
            } else if (traineeAttachment != null) {
                if (isRemoveAllowed) {
                    trainee.removeReimbursementTraineeAttachment(traineeAttachment);
                    fileService.deleteFile(traineeAttachment.getFileLocation());
                } else {
                    gryfValidator.validate(String.format("Nie jest możliwe usunięcie załącznika (dla użytkowników) o nazwie '%s", traineeAttachment.getName()));
                }

                //ADD NEW RECORD
            } else if (traineeAttachmentDTO != null) {
                traineeAttachment = reimbursementTraineeAttachmentRepository.save(new ReimbursementTraineeAttachment());
                fillTraineeAttachment(trainee, traineeAttachment, traineeAttachmentDTO);
                saveTraineeAttachment(trainee, traineeAttachment, traineeAttachmentDTO);
            }
        }
    }

    private boolean isTraineeAttachmentRequired(ReimbursementDelivery delivery, String name) {
        ReimbursementPattern pattern = delivery.getReimbursementPattern();
        for (ReimbursementTraineeAttachmentRequired attachmentRequired : pattern.getReimbursementTraineeAttachmentRequiredList()) {
            if (Objects.equals(attachmentRequired.getName(), name)) {
                return true;
            }
        }
        return false;
    }

    private void saveTraineeAttachment(ReimbursementTrainee trainee, ReimbursementTraineeAttachment traineeAttachment, ReimbursementTraineeAttachmentDTO traineeAttachmentDTO) {
        if (traineeAttachmentDTO.getFile() != null) {
            FileDTO fileDTO = traineeAttachmentDTO.getFile();
            String fileName = reimbursementAttachmentLocalService
                    .findAttachmentName(trainee.getReimbursementTraining().getReimbursement(), ReimbursementTraineeAttachment.ATTACHMENT_TYPE_IN_PATH, traineeAttachment.getId(),
                            traineeAttachment.getName());
            String fileLocation = fileService.writeFile(FileType.REIMBURSEMENTS, fileName, fileDTO, traineeAttachment);
            if (traineeAttachment.getFileLocation() != null && !fileLocation.equals(traineeAttachment.getFileLocation())) {
                fileService.deleteFile(traineeAttachment.getFileLocation());
            }
            traineeAttachment.setFileLocation(fileLocation);
            traineeAttachment.setOriginalFileName(fileDTO.getOriginalFilename());
            traineeAttachmentDTO.setOriginalFileName(fileDTO.getOriginalFilename());
            traineeAttachmentDTO.setFileIncluded(false);
        }
    }

    private void fillTraineeAttachment(ReimbursementTrainee trainee, ReimbursementTraineeAttachment attachment, ReimbursementTraineeAttachmentDTO attachmentDTO) {
        attachment.setName(attachmentDTO.getName());
        attachment.setAttachmentType(attachmentDTO.getAttachmentType() != null ? reimbursementAttachmentTypeRepository.get((String) attachmentDTO.getAttachmentType().getId()) : null);
        attachment.setRemarks(attachmentDTO.getRemarks());
        attachment.setRequired(YesNo.fromBoolean(isTraineeAttachmentRequired(trainee.getReimbursementTraining().getReimbursement().getReimbursementDelivery(), attachment.getName())));
        trainee.addReimbursementTraineeAttachment(attachment);
    }

    private List<ReimbursementTraineeAttachmentTempDTO> makeTraineeAttachmentTempDtoList(List<ReimbursementTraineeAttachment> traineeAttachments,
            List<ReimbursementTraineeAttachmentDTO> traineeAttachmentDtoList) {
        List<ReimbursementTraineeAttachmentTempDTO> result = new ArrayList<>();

        //ATTACHMENTS - MAP <ID, ATTACHMENT>
        Map<Long, ReimbursementTraineeAttachment> traineeAttachmentMap = GryfUtils.constructMap(traineeAttachments, new GryfUtils.MapConstructor<Long, ReimbursementTraineeAttachment>() {

            public boolean isAddToMap(ReimbursementTraineeAttachment input) {
                return input.getId() != null;
            }

            public Long getKey(ReimbursementTraineeAttachment input) {
                return input.getId();
            }
        });

        //ATTACHMENTS DTO - MAP <ID, ATTACHMENT DTO>
        Map<Long, ReimbursementTraineeAttachmentDTO> traineeAttachmentDtoMap = GryfUtils
                .constructMap(traineeAttachmentDtoList, new GryfUtils.MapConstructor<Long, ReimbursementTraineeAttachmentDTO>() {

                    public boolean isAddToMap(ReimbursementTraineeAttachmentDTO input) {
                        return input.getId() != null;
                    }

                    public Long getKey(ReimbursementTraineeAttachmentDTO input) {
                        return input.getId();
                    }
                });

        //MAKE LIST
        for (ReimbursementTraineeAttachmentDTO traineeAttachmentDTO : traineeAttachmentDtoList) {
            ReimbursementTraineeAttachmentTempDTO temp = new ReimbursementTraineeAttachmentTempDTO();
            temp.setTraineeAttachmentDTO(traineeAttachmentDTO);
            temp.setTraineeAttachment((traineeAttachmentDTO.getId() != null) ? traineeAttachmentMap.get(traineeAttachmentDTO.getId()) : null);
            result.add(temp);
        }
        for (ReimbursementTraineeAttachment traineeAttachment : traineeAttachments) {
            if (!traineeAttachmentDtoMap.containsKey(traineeAttachment.getId())) {
                ReimbursementTraineeAttachmentTempDTO temp = new ReimbursementTraineeAttachmentTempDTO();
                temp.setTraineeAttachment(traineeAttachment);
                result.add(temp);
            }
        }

        return result;
    }
}
