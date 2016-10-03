package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.FileContainerDTO;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.basic.GryfDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by tomasz.bilski.ext on 2015-09-07.
 */
@ToString
public class ReimbursementAttachmentDTO extends GryfDto implements FileContainerDTO {

    //FIELDS

    private Long id;

    @NotEmpty(message = "Nazwa załacznika nie może być pusta")
    @Size(message = "Nazwa załacznika może mieć maksymalnie 50 znaków", max = 50)
    private String name;

    private String originalFileName;

    @NotNull(message = "Rodzaj dokumentu nie może być pusty")
    private DictionaryDTO attachmentType;

    @Size(message = "Opis załacznika może mieć maksymalnie 500 znaków", max = 500)
    private String remarks;

    @JsonIgnore
    private FileDTO file;

    private boolean mandatory;

    private boolean fileIncluded;

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
