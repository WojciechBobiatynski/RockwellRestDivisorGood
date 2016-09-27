package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-09-18.
 */
@ToString
@Entity
@Table(name = "ORDER_ELEMENT_CLOBS", schema = "APP_PBE")
public class OrderElementClob extends GryfEntity{

    @Id
    @Column(name = "ORDER_ELEMENT_ID")
    private Long orderElementId;

    @OneToOne
    @JoinColumn(name = "ORDER_ELEMENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private OrderElement orderElement;

    @Column(name = "VALUE_CLOB")
    private String valueClob;

    //GETTERS & SETTERS

    public Long getOrderElementId() {
        return orderElementId;
    }

    public void setOrderElementId(Long orderElementId) {
        this.orderElementId = orderElementId;
    }

    public OrderElement getOrderElement() {
        return orderElement;
    }

    public void setOrderElement(OrderElement orderElement) {
        this.orderElement = orderElement;
    }

    public String getValueClob() {
        return valueClob;
    }

    public void setValueClob(String valueClob) {
        this.valueClob = valueClob;
    }


    //EQUALS & HASH CODE

    @Override
    public int hashCode() {
        return Objects.hashCode(orderElementId) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(orderElementId, ((OrderElementClob) o).orderElementId);
    }
}
