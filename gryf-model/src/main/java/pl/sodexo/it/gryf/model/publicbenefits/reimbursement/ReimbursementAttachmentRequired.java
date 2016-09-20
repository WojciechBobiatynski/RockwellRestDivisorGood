/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@Entity
@Table(name = "REIMBURSEMENT_ATTACHMENTS_REQ", schema = "APP_PBE")
public class ReimbursementAttachmentRequired extends GryfEntity {

    //FIELDS

    @Id
    @NotNull
    @Column(name = "ID")
    private Long id;

    @JoinColumn(name = "REIMBURSEMENT_PATTERN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ReimbursementPattern reimbursementPattern;

    @JoinColumn(name = "ATTACHMENT_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ReimbursementAttachmentType attachmentType;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReimbursementPattern getReimbursementPattern() {
        return reimbursementPattern;
    }

    public void setReimbursementPattern(ReimbursementPattern reimbursementPatternId) {
        this.reimbursementPattern = reimbursementPatternId;
    }

    public ReimbursementAttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(ReimbursementAttachmentType attachmentTypeId) {
        this.attachmentType = attachmentTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return Objects.equals(id, ((ReimbursementAttachmentRequired) o).id);
    }

    @Override
    public String toString() {
        return "pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachmentRequired[ id=" + id + " ]";
    }
    
}
