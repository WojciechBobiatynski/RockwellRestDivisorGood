package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-08.
 */
@Entity
@ToString(exclude = {"trainingInstance", "productInstancePool", "reimbursment"})
@Table(name = "PBE_PRODUCT_INSTANCE_POOL_USES", schema = "APP_PBE")
public class PbeProductInstancePoolUse extends VersionableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @Column(name = "ASSIGNED_NUM")
    private Integer assignedNum;

    @ManyToOne
    @JoinColumn(name = "TRAINING_INSTANCE_ID", referencedColumnName = "ID")
    private TrainingInstance trainingInstance;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_INSTANCE_POOL_ID", referencedColumnName = "ID")
    private PbeProductInstancePool productInstancePool;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAssignedNum() {
        return assignedNum;
    }

    public void setAssignedNum(Integer assignedNum) {
        this.assignedNum = assignedNum;
    }

    public TrainingInstance getTrainingInstance() {
        return trainingInstance;
    }

    public void setTrainingInstance(TrainingInstance trainingInstance) {
        this.trainingInstance = trainingInstance;
    }

    public PbeProductInstancePool getProductInstancePool() {
        return productInstancePool;
    }

    public void setProductInstancePool(PbeProductInstancePool productInstancePool) {
        this.productInstancePool = productInstancePool;
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
        return Objects.equals(id, ((PbeProductInstancePoolUse) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
