package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-07.
 */
@Entity
@ToString(exclude = {"training", "individual"})
@Table(name = "TI_TRAINING_INSTANCES", schema = "APP_PBE")
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
