package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-02.
 */
@Embeddable
public class PbeProductInstancePK implements Serializable {

    //FIELDS

    @Column(name = "PRD_ID")
    private String productId;

    @Column(name = "NUM")
    private Long number;

    //CONSTRUCTORS

    public PbeProductInstancePK() {
    }

    public PbeProductInstancePK(String productId, Long number) {
        this.productId = productId;
        this.number = number;
    }

    //GETTERS & SETTERS

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
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
        return Objects.equals(productId, ((PbeProductInstancePK) o).productId) &&
                Objects.equals(number, ((PbeProductInstancePK) o).number);
    }

    @Override
    public int hashCode() {
        return (Objects.hashCode(productId) % 102 +
                Objects.hashCode(number)) % 102;
    }


}
