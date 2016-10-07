package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@ToString(exclude = {"reimbursementPattern", "attachmentType"})
@Entity
@Table(name = "REIMBURSEMENT_TRAINEE_ATT_REQ", schema = "APP_PBE")
public class ReimbursementTraineeAttachmentRequired extends GryfEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    private Long id;

    @JoinColumn(name = "REIMBURSEMENT_PATTERN_ID", referencedColumnName = "ID")
    @ManyToOne
    private ReimbursementPattern reimbursementPattern;

    @JoinColumn(name = "ATTACHMENT_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
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

    public void setReimbursementPattern(ReimbursementPattern reimbursementPattern) {
        this.reimbursementPattern = reimbursementPattern;
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
        return Objects.equals(id, ((ReimbursementTraineeAttachmentRequired) o).id);
    }
}
