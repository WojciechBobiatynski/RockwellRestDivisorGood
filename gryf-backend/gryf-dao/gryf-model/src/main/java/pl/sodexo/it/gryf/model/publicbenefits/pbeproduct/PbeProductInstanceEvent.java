package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-03.
 */
@Entity
@ToString(exclude = {"productInstance"})
@Table(name = "PBE_PRODUCT_INSTANCE_E", schema = "APP_PBE")
public class PbeProductInstanceEvent extends VersionableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @JoinColumns({
            @JoinColumn(name = "PRD_ID", referencedColumnName = "PRD_ID"),
            @JoinColumn(name = "NUM", referencedColumnName = "NUM")})
    @ManyToOne
    private PbeProductInstance productInstance;

    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private PbeProductInstanceEventType type;

    @Column(name = "SOURCE_ID")
    private String sourceId;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PbeProductInstance getProductInstance() {
        return productInstance;
    }

    public void setProductInstance(PbeProductInstance productInstance) {
        this.productInstance = productInstance;
    }

    public PbeProductInstanceEventType getType() {
        return type;
    }

    public void setType(PbeProductInstanceEventType type) {
        this.type = type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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
        return Objects.equals(id, ((PbeProductInstanceEvent) o).id);
    }
}
