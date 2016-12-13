package pl.sodexo.it.gryf.model.importdata;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.asynch.AsynchronizeJob;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Isolution on 2016-12-01.
 */
@ToString
@Entity
@NamedQueries({
        @NamedQuery(name = "ImportDataRow.getByImportJobAndRowNum", query="select o from ImportDataRow o " +
                "where o.importJob.id = :importJobId " +
                "and o.rowNum = :rowNum ")
})
@Table(name = "IMPORT_DATA_ROWS", schema = "APP_PBE")
public class ImportDataRow extends VersionableEntity {

    public static final int DESCRIPTION_MAX_SIZE = 1000;

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IMPORT_JOB_ID")
    private AsynchronizeJob importJob;

    @Column(name = "ROW_NUM")
    private Integer rowNum;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ImportDataRowStatus status;

    @ManyToOne
    @JoinColumn(name = "CONTRACT_ID")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "INDIVIDUAL_ID")
    private Individual individual;

    @ManyToOne
    @JoinColumn(name = "ENTERPRISE_ID")
    private Enterprise enterprise;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "TRAINING_INSTITUTION_ID")
    private TrainingInstitution trainingInstitution;

    @ManyToOne
    @JoinColumn(name = "TRAINING_ID")
    private Training training;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "row")
    private List<ImportDataRowError> errors;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AsynchronizeJob getImportJob() {
        return importJob;
    }

    public void setImportJob(AsynchronizeJob importJob) {
        this.importJob = importJob;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImportDataRowStatus getStatus() {
        return status;
    }

    public void setStatus(ImportDataRowStatus status) {
        this.status = status;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public TrainingInstitution getTrainingInstitution() {
        return trainingInstitution;
    }

    public void setTrainingInstitution(TrainingInstitution trainingInstitution) {
        this.trainingInstitution = trainingInstitution;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    //LIST METHODS

    private List<ImportDataRowError> getInitializedErrors() {
        if (errors == null)
            errors = new ArrayList<>();
        return errors;
    }

    public List<ImportDataRowError> getErrors() {
        return Collections.unmodifiableList(getInitializedErrors());
    }

    public void addError(ImportDataRowError error) {
        if (error.getRow() != null && error.getRow() != this) {
            error.getRow().getInitializedErrors().remove(error);
        }
        if (error.getId() == null || !getInitializedErrors().contains(error)) {
            getInitializedErrors().add(error);
        }
        error.setRow(this);
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
        return Objects.equals(id, ((ImportDataRow) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
