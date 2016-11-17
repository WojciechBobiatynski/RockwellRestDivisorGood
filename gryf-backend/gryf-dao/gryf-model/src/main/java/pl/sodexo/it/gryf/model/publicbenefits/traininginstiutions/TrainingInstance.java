package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.EnterpriseContact;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.pbeproduct.PbeProductInstancePoolUse;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by Isolution on 2016-11-07.
 */
@Entity
@ToString(exclude = {"training", "individual"})
@Table(name = "TI_TRAINING_INSTANCES", schema = "APP_PBE")
@SequenceGenerator(name="ti_tra_ins_seq", schema = "eagle", sequenceName = "ti_tra_ins_seq", allocationSize = 1)
public class TrainingInstance extends VersionableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ti_tra_ins_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TRAINING_ID", referencedColumnName = "ID")
    private Training training;

    @ManyToOne
    @JoinColumn(name = "INDIVIDUAL_ID", referencedColumnName = "ID")
    private Individual individual;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID", referencedColumnName = "ID")
    private GrantProgram grantProgram;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
    private TrainingInstanceStatus status;

    @Column(name = "ASSIGNED_NUM")
    private Integer assignedNum;

    @Column(name = "REGISTER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Column(name = "REIMBURSMENT_PIN")
    private String reimbursmentPin;

    @Column(name = "E_REIMBURSMENT_ID")
    private Long reimbursment;

    @OneToMany(mappedBy = "trainingInstance")
    private List<PbeProductInstancePoolUse> pollUses;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public TrainingInstanceStatus getStatus() {
        return status;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public GrantProgram getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgram grantProgram) {
        this.grantProgram = grantProgram;
    }

    public void setStatus(TrainingInstanceStatus status) {
        this.status = status;
    }

    public Integer getAssignedNum() {
        return assignedNum;
    }

    public void setAssignedNum(Integer assignedNum) {
        this.assignedNum = assignedNum;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getReimbursmentPin() {
        return reimbursmentPin;
    }

    public void setReimbursmentPin(String reimbursmentPin) {
        this.reimbursmentPin = reimbursmentPin;
    }

    public Long getReimbursment() {
        return reimbursment;
    }

    public void setReimbursment(Long reimbursment) {
        this.reimbursment = reimbursment;
    }

    //LIST METHODS

    private List<PbeProductInstancePoolUse> getInitializedPollUsesList() {
        if (pollUses == null)
            pollUses = new ArrayList<>();
        return pollUses;
    }

    public List<PbeProductInstancePoolUse> getPollUses() {
        return Collections.unmodifiableList(getInitializedPollUsesList());
    }

    public void addPollUse(PbeProductInstancePoolUse pollUse) {
        if (pollUse.getTrainingInstance() != null && pollUse.getTrainingInstance() != this) {
            pollUse.getTrainingInstance().getInitializedPollUsesList().remove(pollUse);
        }
        if (pollUse.getId() == null || !getInitializedPollUsesList().contains(pollUse)) {
            getInitializedPollUsesList().add(pollUse);
        }
        pollUse.setTrainingInstance(this);
    }

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((TrainingInstance) o).id);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }
}
