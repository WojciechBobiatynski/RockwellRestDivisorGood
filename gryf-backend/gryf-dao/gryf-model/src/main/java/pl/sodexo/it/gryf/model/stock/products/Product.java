/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.stock.products;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantOwnerAidProduct;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@ToString(exclude = "grantOwnerAidProduct")
@Entity
@Table(name = "PRODUCTS", schema = "APP_STC")
public class Product extends GryfEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ID")
    private String id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TITLE")
    private String title;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;

    @Column(name = "QUANTITY")
    private Long quantity;

    @Column(name = "VALUE")
    private BigDecimal value;

    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EXPIRED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredDate;

    @Size(max = 10)
    @Column(name = "VAT")
    private String vat;

    @Size(max = 7)
    @Column(name = "DEP_CONS")
    private String depCons;

    @Column(name = "PH_GROUP")
    private Integer phGroup;

    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "CREATED")
    private String created;

    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "MODIFIED")
    private String modified;

    @Column(name = "DELIVERED")
    private Long delivered;

    @Column(name = "DESTROYED")
    private Long destroyed;

    @Column(name = "GENERATED")
    private Long generated;

    @Column(name = "IN_OPERATOR")
    private Long inOperator;

    @Column(name = "ON_STOCK")
    private Long onStock;

    @Column(name = "REIMBURSED")
    private Long reimbursed;

    @Column(name = "RETURNED")
    private Long returned;

    @Size(max = 1)
    @Column(name = "ACTIVE")
    private String active;

    @Column(name = "REM_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date remDate;

    @Column(name = "EMISSION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date emissionDate;

    @Size(max = 1)
    @Column(name = "MANUAL")
    private String manual;

    @Size(max = 1)
    @Column(name = "COMPOSIT")
    private String composit;

    @Column(name = "CANCELLED_GENERATED")
    private Long cancelledGenerated;

    @Size(max = 1)
    @Column(name = "NOT_STANDARD")
    private String notStandard;

    @Basic(optional = false)
    @NotNull
    @Column(name = "WEIGHT")
    private BigDecimal weight;

    @Column(name = "PBE_TOTAL_VALUE")
    private BigDecimal pbeTotalValue;

    @Column(name = "PBE_AID_VALUE")
    private BigDecimal pbeAidValue;

    @JoinColumn(name = "PBE_AID_PRODUCT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GrantOwnerAidProduct grantOwnerAidProduct;

    //GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getDepCons() {
        return depCons;
    }

    public void setDepCons(String depCons) {
        this.depCons = depCons;
    }

    public Integer getPhGroup() {
        return phGroup;
    }

    public void setPhGroup(Integer phGroup) {
        this.phGroup = phGroup;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Long getDelivered() {
        return delivered;
    }

    public void setDelivered(Long delivered) {
        this.delivered = delivered;
    }

    public Long getDestroyed() {
        return destroyed;
    }

    public void setDestroyed(Long destroyed) {
        this.destroyed = destroyed;
    }

    public Long getGenerated() {
        return generated;
    }

    public void setGenerated(Long generated) {
        this.generated = generated;
    }

    public Long getInOperator() {
        return inOperator;
    }

    public void setInOperator(Long inOperator) {
        this.inOperator = inOperator;
    }

    public Long getOnStock() {
        return onStock;
    }

    public void setOnStock(Long onStock) {
        this.onStock = onStock;
    }

    public Long getReimbursed() {
        return reimbursed;
    }

    public void setReimbursed(Long reimbursed) {
        this.reimbursed = reimbursed;
    }

    public Long getReturned() {
        return returned;
    }

    public void setReturned(Long returned) {
        this.returned = returned;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Date getRemDate() {
        return remDate;
    }

    public void setRemDate(Date remDate) {
        this.remDate = remDate;
    }

    public Date getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(Date emissionDate) {
        this.emissionDate = emissionDate;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    public String getComposit() {
        return composit;
    }

    public void setComposit(String composit) {
        this.composit = composit;
    }

    public Long getCancelledGenerated() {
        return cancelledGenerated;
    }

    public void setCancelledGenerated(Long cancelledGenerated) {
        this.cancelledGenerated = cancelledGenerated;
    }

    public String getNotStandard() {
        return notStandard;
    }

    public void setNotStandard(String notStandard) {
        this.notStandard = notStandard;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getPbeTotalValue() {
        return pbeTotalValue;
    }

    public void setPbeTotalValue(BigDecimal pbeTotalValue) {
        this.pbeTotalValue = pbeTotalValue;
    }

    public BigDecimal getPbeAidValue() {
        return pbeAidValue;
    }

    public void setPbeAidValue(BigDecimal pbeAidValue) {
        this.pbeAidValue = pbeAidValue;
    }

    public GrantOwnerAidProduct getGrantOwnerAidProduct() {
        return grantOwnerAidProduct;
    }

    public void setGrantOwnerAidProduct(GrantOwnerAidProduct grantOwnerAidProduct) {
        this.grantOwnerAidProduct = grantOwnerAidProduct;
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
        return Objects.equals(id, ((Product) o).id);
    }
}
