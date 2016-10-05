package pl.sodexo.it.gryf.common.parsers;

import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.*;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-09.
 */
public class ReimbursementParser {

    //ORDERS

    public static ReimbursementDTO readReimbursement(String data, List<FileDTO> fileDtoList) {
        ReimbursementDTO dto = JsonMapperUtils.readValue(data, ReimbursementDTO.class);

        //MATCH WITH FILE
        int index = 0;
        if(dto.getReimbursementAttachments() != null) {
            for (ReimbursementAttachmentDTO attachmentDTO : dto.getReimbursementAttachments()) {
                if (attachmentDTO.isFileIncluded()) {
                    attachmentDTO.setFile(fileDtoList.get(index++));
                    attachmentDTO.getFile().setAttachmentName(attachmentDTO.getName());
                }
            }
        }
        if(dto.getReimbursementTrainings() != null) {
            for (ReimbursementTrainingDTO trainingDTO : dto.getReimbursementTrainings()) {
                if(trainingDTO.getReimbursementTrainees() != null) {
                    for (ReimbursementTraineeDTO traineeDTO : trainingDTO.getReimbursementTrainees()) {
                        if(traineeDTO.getReimbursementTraineeAttachments() != null) {
                            for (ReimbursementTraineeAttachmentDTO attachmentDTO : traineeDTO.getReimbursementTraineeAttachments()) {
                                if (attachmentDTO.isFileIncluded()) {
                                    attachmentDTO.setFile(fileDtoList.get(index++));
                                    attachmentDTO.getFile().setAttachmentName(attachmentDTO.getName());
                                }
                            }
                        }
                    }
                }
            }
        }

        return dto;
    }
}
