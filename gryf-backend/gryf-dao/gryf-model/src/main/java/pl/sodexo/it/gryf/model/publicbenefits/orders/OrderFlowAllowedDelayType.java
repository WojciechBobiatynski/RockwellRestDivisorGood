package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@ToString(exclude = "delayStartingPointType")
@Entity
@Table(name = "ORDER_FLOW_ALLOWED_DELAY_TYPES", schema = "APP_PBE")
public class OrderFlowAllowedDelayType extends GryfEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ID")
    private String id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "DELAY_STARTING_POINT_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrderFlowDelayStPntType delayStartingPointType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderFlowDelayStPntType getDelayStartingPointType() {
        return delayStartingPointType;
    }

    public void setDelayStartingPointType(OrderFlowDelayStPntType delayStartingPointType) {
        this.delayStartingPointType = delayStartingPointType;
    }

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
        return Objects.equals(id, ((OrderFlowAllowedDelayType) o).id);
    }
}
