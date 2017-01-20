package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-02.
 */
@Entity
@ToString(exclude = {"productEmission", "productInstancePool", "productInstancePoolUse"})
@Table(name = "PBE_PRODUCT_INSTANCES", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = "PbeProductInstance.findAvaiableByProduct", query = "select pi from PbeProductInstance pi " +
                                    "where pi.productEmission.product.id = :productId  and pi.status.id = :statusId order by pi.productEmission.emissionDate, pi.id.number"),
        @NamedQuery(name = "PbeProductInstance.findAssignedByPool", query = "select pi from PbeProductInstance pi " +
                "where pi.productInstancePool.id = :poolId and pi.status.id = :statusId order by pi.id.number"),
        @NamedQuery(name = "PbeProductInstance.findByPoolAndStatus", query = "select pi from PbeProductInstance pi " +
                "where pi.productInstancePool.id = :poolId and pi.status.id = :statusId order by pi.id.number")})

public class PbeProductInstance extends VersionableEntity {

    @EmbeddedId
    private PbeProductInstancePK id;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
    private PbeProductInstanceStatus status;

    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @Column(name = "PRINT_NUM")
    private String printNumber;

    @Column(name = "CRC")
    private Integer crc;

    @JoinColumn(name = "PRODUCT_EMISSION_ID", referencedColumnName = "ID")
    @ManyToOne
    private PbeProductEmission productEmission;

    @JoinColumn(name = "PRODUCT_INSTANCE_POOL_ID", referencedColumnName = "ID")
    @ManyToOne
    private PbeProductInstancePool productInstancePool;

    @JoinColumn(name = "PRODUCT_INSTANCE_POOL_USE_ID", referencedColumnName = "ID")
    @ManyToOne
    private PbeProductInstancePoolUse productInstancePoolUse;

    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "E_REIMBURSMENT_ID")
    private Long ereimbursmentId;

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

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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

    public PbeProductEmission getProductEmission() {
        return productEmission;
    }

    public void setProductEmission(PbeProductEmission productEmission) {
        this.productEmission = productEmission;
    }

    public PbeProductInstancePool getProductInstancePool() {
        return productInstancePool;
    }

    public void setProductInstancePool(PbeProductInstancePool productInstancePool) {
        this.productInstancePool = productInstancePool;
    }

    public PbeProductInstancePoolUse getProductInstancePoolUse() {
        return productInstancePoolUse;
    }

    public void setProductInstancePoolUse(PbeProductInstancePoolUse productInstancePoolUse) {
        this.productInstancePoolUse = productInstancePoolUse;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getEreimbursmentId() {
        return ereimbursmentId;
    }

    public void setEreimbursmentId(Long electronicReimbursmentId) {
        this.ereimbursmentId = electronicReimbursmentId;
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
