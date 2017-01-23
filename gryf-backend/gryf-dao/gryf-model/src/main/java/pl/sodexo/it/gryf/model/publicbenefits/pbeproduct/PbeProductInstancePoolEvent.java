package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.CreationAuditedEntity;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-04.
 */
@Entity
@ToString(exclude = {"productInstancePool"})
@Table(name = "PBE_PRODUCT_INSTANCE_POOL_E", schema = "APP_PBE")
public class PbeProductInstancePoolEvent extends CreationAuditedEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @JoinColumn(name = "PRODUCT_INSTANCE_POOL_ID", referencedColumnName = "ID")
    @ManyToOne
    private PbeProductInstancePool productInstancePool;

    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private PbeProductInstancePoolEventType type;

    @Column(name = "SOURCE_ID")
    private Long sourceId;

    @Column(name = "NUM")
    private Integer num;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PbeProductInstancePool getProductInstancePool() {
        return productInstancePool;
    }

    public void setProductInstancePool(PbeProductInstancePool productInstancePool) {
        this.productInstancePool = productInstancePool;
    }

    public PbeProductInstancePoolEventType getType() {
        return type;
    }

    public void setType(PbeProductInstancePoolEventType type) {
        this.type = type;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    //HASH CODE & EQUALS

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
        return Objects.equals(id, ((PbeProductInstancePoolEvent) o).id);
    }
}
