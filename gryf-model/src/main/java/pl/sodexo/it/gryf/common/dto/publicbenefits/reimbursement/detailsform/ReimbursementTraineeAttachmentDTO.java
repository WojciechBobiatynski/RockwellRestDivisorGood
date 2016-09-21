package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.FileContainerDTO;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.YesNo;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraineeAttachmentRequired;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-08.
 */
public class ReimbursementTraineeAttachmentDTO implements FileContainerDTO {

    //FIELDS

    private Long id;

    @NotEmpty(message = "Nazwa załacznika (dla uczestników) nie może być pusta")
    @Size(message = "Nazwa załacznika (dla uczestników) może mieć maksymalnie 50 znaków", max = 50)
    private String name;

    private String originalFileName;

    @NotNull(message = "Rodzaj dokumentu (dla załączników uczestników) nie może być pusty")
    private DictionaryDTO attachmentType;

    @Size(message = "Opis załacznika (dla uczestników) może mieć maksymalnie 500 znaków", max = 500)
    private String remarks;

    @JsonIgnore
    private FileDTO file;

    private boolean mandatory;

    private boolean fileIncluded;

    //CONSTRUCTORS

    public ReimbursementTraineeAttachmentDTO() {
    }

    public ReimbursementTraineeAttachmentDTO(ReimbursementTraineeAttachment entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.originalFileName = entity.getOriginalFileName();
        this.attachmentType = DictionaryDTO.create(entity.getAttachmentType());
        this.remarks = entity.getRemarks();
        this.file = null;
        this.mandatory = YesNo.toBoolean(entity.getRequired());;
        this.fileIncluded = false;
    }

    public ReimbursementTraineeAttachmentDTO(ReimbursementTraineeAttachmentRequired entity) {
        this.id = null;
        this.name = entity.getName();
        this.originalFileName = null;
        this.attachmentType = DictionaryDTO.create(entity.getAttachmentType());
        this.remarks = null;
        this.file = null;
        this.mandatory = true;
        this.fileIncluded = false;
    }

    //STATIC METHODS - CREATE

    public static ReimbursementTraineeAttachmentDTO createAttachment(ReimbursementTraineeAttachment entity) {
        return entity != null ? new ReimbursementTraineeAttachmentDTO(entity) : null;
    }

    public static List<ReimbursementTraineeAttachmentDTO> createAttachmentList(List<ReimbursementTraineeAttachment> entities) {
        List<ReimbursementTraineeAttachmentDTO> list = new ArrayList<>();
        for (ReimbursementTraineeAttachment entity : entities) {
            list.add(createAttachment(entity));
        }
        return list;
    }

    public static ReimbursementTraineeAttachmentDTO createAttachmentRequired(ReimbursementTraineeAttachmentRequired entity) {
        return entity != null ? new ReimbursementTraineeAttachmentDTO(entity) : null;
    }

    public static List<ReimbursementTraineeAttachmentDTO> createAttachmentRequiredList(List<ReimbursementTraineeAttachmentRequired> entities) {
        List<ReimbursementTraineeAttachmentDTO> list = new ArrayList<>();
        for (ReimbursementTraineeAttachmentRequired entity : entities) {
            list.add(createAttachmentRequired(entity));
        }
        return list;
    }

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public DictionaryDTO getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(DictionaryDTO attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public FileDTO getFile() {
        return file;
    }

    public void setFile(FileDTO file) {
        this.file = file;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isFileIncluded() {
        return fileIncluded;
    }

    public void setFileIncluded(boolean fileIncluded) {
        this.fileIncluded = fileIncluded;
    }
}
