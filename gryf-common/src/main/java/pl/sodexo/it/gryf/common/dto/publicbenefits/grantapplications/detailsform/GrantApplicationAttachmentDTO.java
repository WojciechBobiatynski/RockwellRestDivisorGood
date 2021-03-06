package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.api.FileContainerDTO;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.validation.publicbenefits.grantapplication.ValidationGroupApplyOptionalApplication;
import pl.sodexo.it.gryf.common.validation.publicbenefits.grantapplication.ValidationGroupNewGrantApplication;

import javax.validation.constraints.AssertTrue;

/**
 * Created by tomasz.bilski.ext on 2015-07-07.
 */
@ToString
public class GrantApplicationAttachmentDTO implements FileContainerDTO {

    //FIELDS

    private Long id;

    @NotEmpty(message = "Nazwa załącznika nie może być pusta", groups = ValidationGroupApplyOptionalApplication.class)
    private String name;

    private String originalFileName;

    private String remarks;

    @JsonIgnore
    private FileDTO file;

    private boolean mandatory;

    private boolean fileIncluded;

    //PUBLIC METHODS

    @JsonIgnore
    @AssertTrue(message = "Nazwa załącznika musi być wypełniona gdy dodawany jest załącznik", groups = ValidationGroupNewGrantApplication.class)
    public boolean isNameWithFileFill(){
        if(file != null){
            return !GryfStringUtils.isEmpty(name);
        }
        return true;
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

    public boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean getFileIncluded() {
        return fileIncluded;
    }

    public void setFileIncluded(boolean fileIncluded) {
        this.fileIncluded = fileIncluded;
    }
}
