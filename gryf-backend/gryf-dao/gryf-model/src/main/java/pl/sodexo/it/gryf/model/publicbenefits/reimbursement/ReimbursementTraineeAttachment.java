/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import lombok.ToString;
import pl.sodexo.it.gryf.common.enums.YesNo;
import pl.sodexo.it.gryf.model.api.AuditableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@ToString(exclude = {"reimbursementTrainee", "attachmentType"})
@Entity
@Table(name = "REIMBURSEMENT_TRAINEE_ATTACH", schema = "APP_PBE")
public class ReimbursementTraineeAttachment extends AuditableEntity {

    //STATIC FIELDS
    public static final String ATTACHMENT_TYPE_IN_PATH = "TRAINEE";

    //FIELDS

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @JoinColumn(name = "REIMBURSEMENT_TRAINEE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ReimbursementTrainee reimbursementTrainee;

    @JoinColumn(name = "ATTACHMENT_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ReimbursementAttachmentType attachmentType;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;

    @Size(max = 255)
    @Column(name = "ORIGINAL_FILE_NAME")
    private String originalFileName;

    @Size(max = 200)
    @Column(name = "FILE_LOCATION")
    private String fileLocation;

    @Enumerated(EnumType.STRING)
    @Column(name="REQUIRED")
    private YesNo required;

    @Size(max = 500)
    @Column(name = "REMARKS")
    private String remarks;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReimbursementTrainee getReimbursementTrainee() {
        return reimbursementTrainee;
    }

    public void setReimbursementTrainee(ReimbursementTrainee reimbursementTrainee) {
        this.reimbursementTrainee = reimbursementTrainee;
    }

    public ReimbursementAttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(ReimbursementAttachmentType attachmentType) {
        this.attachmentType = attachmentType;
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

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public YesNo getRequired() {
        return required;
    }

    public void setRequired(YesNo required) {
        this.required = required;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    //EQUALS & HASH CODE

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((ReimbursementTraineeAttachment) o).id);
    }
}
