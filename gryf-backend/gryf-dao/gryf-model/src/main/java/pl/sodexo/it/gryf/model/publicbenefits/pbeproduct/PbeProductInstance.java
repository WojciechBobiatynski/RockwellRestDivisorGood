package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-02.
 */
@Entity
@Table(name = "PBE_PRODUCT_INSTANCES", schema = "APP_PBE")
public class PbeProductInstance extends VersionableEntity {

    @EmbeddedId
    private PbeProductInstancePK id;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
    private PbeProductInstanceStatus status;

    @Column(name = "PRINT_NUM")
    private String printNumber;

    @Column(name = "CRC")
    private Integer crc;

    @JoinColumn(name = "PRODUCT_EMISSION_ID", referencedColumnName = "ID")
    @ManyToOne
    private PbeProductEmission productEmission;

    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    @ManyToOne
    private Order order;

    @Column(name = "E_REIMBURSMENT_ID")
    private Long electonicReimbursment;

    //GETTERS & SETTERS

    public PbeProductInstancePK getId() {
        return id;
    }

    public void setId(PbeProductInstancePK id) {
        this.id = id;
    }

    public PbeProductInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(PbeProductInstanceStatus status) {
        this.status = status;
    }

    public String getPrintNumber() {
        return printNumber;
    }

    public void setPrintNumber(String printNumber) {
        this.printNumber = printNumber;
    }

    public Integer getCrc() {
        return crc;
    }

    public void setCrc(Integer crc) {
        this.crc = crc;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PbeProductEmission getProductEmission() {
        return productEmission;
    }

    public void setProductEmission(PbeProductEmission productEmission) {
        this.productEmission = productEmission;
    }

    public Long getElectonicReimbursment() {
        return electonicReimbursment;
    }

    public void setElectonicReimbursment(Long electonicReimbursment) {
        this.electonicReimbursment = electonicReimbursment;
    }

    //EQUALS & HASH CODE

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
        return Objects.equals(id, ((PbeProductInstance) o).id);
    }
}
