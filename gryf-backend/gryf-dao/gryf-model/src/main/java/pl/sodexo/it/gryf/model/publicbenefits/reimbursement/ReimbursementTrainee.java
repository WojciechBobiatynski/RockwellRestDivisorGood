/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.AuditableEntity;
import pl.sodexo.it.gryf.model.enums.Sex;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@ToString(exclude = {"reimbursementTraining", "reimbursementTraineeAttachments"})
@Entity
@Table(name = "REIMBURSEMENT_TRAINEES",  schema = "APP_PBE")
public class ReimbursementTrainee extends AuditableEntity {

    //FIELDS

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @JoinColumn(name = "REIMBURSEMENT_TRAINING_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ReimbursementTraining reimbursementTraining;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "TRAINEE_NAME")
    private String traineeName;

    @NotNull
    @Column(name = "TRAINEE_SEX")
    @Enumerated(EnumType.STRING)
    private Sex traineeSex;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reimbursementTrainee", orphanRemoval = true)
    private List<ReimbursementTraineeAttachment> reimbursementTraineeAttachments;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReimbursementTraining getReimbursementTraining() {
        return reimbursementTraining;
    }

    public void setReimbursementTraining(ReimbursementTraining reimbursementTraining) {
        this.reimbursementTraining = reimbursementTraining;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    public Sex getTraineeSex() {
        return traineeSex;
    }

    public void setTraineeSex(Sex traineeSex) {
        this.traineeSex = traineeSex;
    }

    //LIST METHODS

    private List<ReimbursementTraineeAttachment> getInitializedReimbursementTraineeAttachments() {
        if (reimbursementTraineeAttachments == null)
            reimbursementTraineeAttachments = new ArrayList<>();
        return reimbursementTraineeAttachments;
    }

    public List<ReimbursementTraineeAttachment> getReimbursementTraineeAttachments() {
        return Collections.unmodifiableList(getInitializedReimbursementTraineeAttachments());
    }

    public void addReimbursementTraineeAttachment(ReimbursementTraineeAttachment traineeAttachment) {
        if (traineeAttachment.getReimbursementTrainee() != null && traineeAttachment.getReimbursementTrainee() != this) {
            traineeAttachment.getReimbursementTrainee().getInitializedReimbursementTraineeAttachments().remove(traineeAttachment);
        }
        if (traineeAttachment.getId() == null || !getInitializedReimbursementTraineeAttachments().contains(traineeAttachment)) {
            getInitializedReimbursementTraineeAttachments().add(traineeAttachment);
        }
        traineeAttachment.setReimbursementTrainee(this);
    }

    public void removeReimbursementTraineeAttachment(ReimbursementTraineeAttachment traineeAttachment) {
        getInitializedReimbursementTraineeAttachments().remove(traineeAttachment);
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
        return Objects.equals(id, ((ReimbursementTrainee) o).id);
    }
}
