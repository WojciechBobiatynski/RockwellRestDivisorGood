package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-04.
 */
@Entity
@ToString(exclude = {"individual", "order", "product"})
@Table(name = "PBE_PRODUCT_INSTANCE_POOLS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = "PbeProductInstancePool.findAvaiableForUse", query = "select p from PbeProductInstancePool p " +
                "where p.individual.id = :individualId and p.order.contract.grantProgram.id = :grantProgramId and p.status.id = :statusId and p.expiryDate <= :expiryDate order by p.expiryDate")})

public class PbeProductInstancePool extends VersionableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
    private PbeProductInstancePoolStatus status;

    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @Column(name = "ALL_NUM")
    private Integer allNum;

    @Column(name = "AVAILABLE_NUM")
    private Integer availableNum;

    @Column(name = "RESERVED_NUM")
    private Integer reservedNum;

    @Column(name = "USED_NUM")
    private Integer usedNum;

    @Column(name = "REMBURS_NUM")
    private Integer rembursNum;

    @ManyToOne
    @JoinColumn(name = "INDIVIDUAL_ID")
    private Individual individual;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @OneToOne
    @JoinColumn(name = "PRD_ID")
    private PbeProduct product;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PbeProductInstancePoolStatus getStatus() {
        return status;
    }

    public void setStatus(PbeProductInstancePoolStatus status) {
        this.status = status;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Integer getAvailableNum() {
        return availableNum;
    }

    public void setAvailableNum(Integer availableNum) {
        this.availableNum = availableNum;
    }

    public Integer getReservedNum() {
        return reservedNum;
    }

    public void setReservedNum(Integer reservedNum) {
        this.reservedNum = reservedNum;
    }

    public Integer getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Integer usedNum) {
        this.usedNum = usedNum;
    }

    public Integer getRembursNum() {
        return rembursNum;
    }

    public void setRembursNum(Integer rembursNum) {
        this.rembursNum = rembursNum;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public PbeProduct getProduct() {
        return product;
    }

    public void setProduct(PbeProduct product) {
        this.product = product;
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
        return Objects.equals(id, ((PbeProductInstancePool) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
