package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@ToString
@Embeddable
public class OrderFlowElementInStatusPK implements Serializable {

    //FIELDS

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "STATUS_ID")
    private String statusId;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ELEMENT_ID")
    private String elementId;

    //CONSTRUCTORS

    public OrderFlowElementInStatusPK() {
    }

    public OrderFlowElementInStatusPK(String statusId, String elementId) {
        this.statusId = statusId;
        this.elementId = elementId;
    }

    //GETTERS & SETTERS

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    //HASH CODE & EQUALS

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(statusId, ((OrderFlowElementInStatusPK) o).statusId) &&
                Objects.equals(elementId, ((OrderFlowElementInStatusPK) o).elementId);
    }

    @Override
    public int hashCode() {
        return (Objects.hashCode(statusId) % 102 +
                Objects.hashCode(elementId)) % 102;
    }
}
