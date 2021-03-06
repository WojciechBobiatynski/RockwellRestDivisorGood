package pl.sodexo.it.gryf.model.publicbenefits.pbeproduct;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
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
                "where p.individual.id = :individualId and p.order.contract.grantProgram.id = :grantProgramId and p.availableNum > 0 " +
                "and p.startDate <= :startDate and :expiryDate <= p.expiryDate order by p.expiryDate, p.id")})

public class PbeProductInstancePool extends VersionableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;

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

    @Column(name = "EXPIRED_NUM")
    private Integer expiredNum;

    @Column(name = "RETURNED_NUM")
    private Integer returnedNum;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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

    public Integer getExpiredNum() {
        return expiredNum;
    }

    public void setExpiredNum(Integer expiredNum) {
        this.expiredNum = expiredNum;
    }

    public Integer getReturnedNum() {
        return returnedNum;
    }

    public void setReturnedNum(Integer returnedNum) {
        this.returnedNum = returnedNum;
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
