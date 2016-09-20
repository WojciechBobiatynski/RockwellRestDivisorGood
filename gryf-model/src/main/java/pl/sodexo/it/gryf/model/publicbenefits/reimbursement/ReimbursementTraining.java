/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.model.AuditableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantOwnerAidProduct;
import pl.sodexo.it.gryf.model.stock.products.RemittanceProductInstances;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@Entity
@Table(name = "REIMBURSEMENT_TRAININGS", schema = "APP_PBE")
public class ReimbursementTraining extends AuditableEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @JoinColumn(name = "REIMBURSEMENT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Reimbursement reimbursement;

    @Size(max = 200)
    @Column(name = "TRAINING_NAME")
    private String trainingName;

    @Column(name = "TRAINING_DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trainingDateFrom;

    @Column(name = "TRAINING_DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trainingDateTo;

    @Size(max = 100)
    @Column(name = "TRAINING_PLACE")
    private String trainingPlace;

    @JoinColumn(name = "AID_PRODUCT_ID", referencedColumnName = "ID")
    @ManyToOne
    private GrantOwnerAidProduct grantOwnerAidProduct;

    @Column(name = "PRODUCTS_NUMBER")
    private BigDecimal productsNumber;

    /**
     * Cena brutto godziny szkolenia z systemu RUR
     */
    @Column(name = "TRAINING_HOUR_GROSS_PRICE")
    private BigDecimal trainingHourGrossPrice;

    /**
     * Sumaryczna ilość godzin szkolenia dla wszystkich uczestników
     */
    @Column(name = "TRAINING_HOURS_TOTAL")
    private BigDecimal trainingHoursTotal;

    /**
     * Wartość produktu pomocy publicznej
     */
    @Column(name = "PRODUCT_TOTAL_VALUE")
    private BigDecimal productTotalValue;

    /**
     * Wartość pomocy w produkcie pomocy publicznej
     */
    @Column(name = "PRODUCT_AID_VALUE")
    private BigDecimal productAidValue;

    /**
     * Kwota należna IS od Sodexo
     */
    @Column(name = "SXO_TI_AMOUNT_DUE")
    private BigDecimal sxoTiAmountDue;

    /**
     * Kwota należna MSP od Sodexo
     */
    @Column(name = "SXO_ENT_AMOUNT_DUE")
    private BigDecimal sxoEntAmountDue;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reimbursementTraining", orphanRemoval = true)
    private List<ReimbursementTrainee> reimbursementTrainees;

    @OneToMany(mappedBy = "reimbursementTraining")
    private List<RemittanceProductInstances> remittanceProductInstances;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reimbursement getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(Reimbursement reimbursement) {
        this.reimbursement = reimbursement;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Date getTrainingDateFrom() {
        return trainingDateFrom;
    }

    public void setTrainingDateFrom(Date trainingDateFrom) {
        this.trainingDateFrom = trainingDateFrom;
    }

    public Date getTrainingDateTo() {
        return trainingDateTo;
    }

    public void setTrainingDateTo(Date trainingDateTo) {
        this.trainingDateTo = trainingDateTo;
    }

    public String getTrainingPlace() {
        return trainingPlace;
    }

    public void setTrainingPlace(String trainingPlace) {
        this.trainingPlace = trainingPlace;
    }

    public GrantOwnerAidProduct getGrantOwnerAidProduct() {
        return grantOwnerAidProduct;
    }

    public void setGrantOwnerAidProduct(GrantOwnerAidProduct grantOwnerAidProduct) {
        this.grantOwnerAidProduct = grantOwnerAidProduct;
    }

    public BigDecimal getProductsNumber() {
        return productsNumber;
    }

    public void setProductsNumber(BigDecimal productsNumber) {
        this.productsNumber = productsNumber;
    }

    public BigDecimal getTrainingHourGrossPrice() {
        return trainingHourGrossPrice;
    }

    public void setTrainingHourGrossPrice(BigDecimal trainingHourGrossPrice) {
        this.trainingHourGrossPrice = trainingHourGrossPrice;
    }

    public BigDecimal getTrainingHoursTotal() {
        return trainingHoursTotal;
    }

    public void setTrainingHoursTotal(BigDecimal trainingHoursTotal) {
        this.trainingHoursTotal = trainingHoursTotal;
    }

    public BigDecimal getProductTotalValue() {
        return productTotalValue;
    }

    public void setProductTotalValue(BigDecimal productTotalValue) {
        this.productTotalValue = productTotalValue;
    }

    public BigDecimal getProductAidValue() {
        return productAidValue;
    }

    public void setProductAidValue(BigDecimal productAidValue) {
        this.productAidValue = productAidValue;
    }

    public BigDecimal getSxoTiAmountDue() {
        return sxoTiAmountDue;
    }

    public void setSxoTiAmountDue(BigDecimal sxoTiAmountDue) {
        this.sxoTiAmountDue = sxoTiAmountDue;
    }

    public BigDecimal getSxoEntAmountDue() {
        return sxoEntAmountDue;
    }

    public void setSxoEntAmountDue(BigDecimal sxoEntAmountDue) {
        this.sxoEntAmountDue = sxoEntAmountDue;
    }

    //LIST METHODS

    private List<ReimbursementTrainee> getInitializedReimbursementTrainees() {
        if (reimbursementTrainees == null)
            reimbursementTrainees = new ArrayList<>();
        return reimbursementTrainees;
    }

    public List<ReimbursementTrainee> getReimbursementTrainees() {
        return Collections.unmodifiableList(getInitializedReimbursementTrainees());
    }

    public void addReimbursementTrainee(ReimbursementTrainee trainee) {
        if (trainee.getReimbursementTraining() != null && trainee.getReimbursementTraining() != this) {
            trainee.getReimbursementTraining().getInitializedReimbursementTrainees().remove(trainee);
        }
        if (trainee.getId() == null || !getInitializedReimbursementTrainees().contains(trainee)) {
            getInitializedReimbursementTrainees().add(trainee);
        }
        trainee.setReimbursementTraining(this);
    }

    public void removeReimbursementTrainee(ReimbursementTrainee trainee) {
        getInitializedReimbursementTrainees().remove(trainee);
    }

    private List<RemittanceProductInstances> getInitializedRemittanceProductInstances() {
        if (remittanceProductInstances == null)
            remittanceProductInstances = new ArrayList<>();
        return remittanceProductInstances;
    }

    public List<RemittanceProductInstances> getRemittanceProductInstances() {
        return Collections.unmodifiableList(getInitializedRemittanceProductInstances());
    }

    public void addRemittanceProductInstance(RemittanceProductInstances product) {
        if (product.getReimbursementTraining() != null && product.getReimbursementTraining() != this) {
            product.getReimbursementTraining().getInitializedRemittanceProductInstances().remove(product);
        }
        if (product.getRowid() == null || !getInitializedRemittanceProductInstances().contains(product)) {
            getInitializedRemittanceProductInstances().add(product);
        }
        product.setReimbursementTraining(this);
    }

    public void removeRemittanceProductInstance(RemittanceProductInstances product) {
        getInitializedRemittanceProductInstances().remove(product);
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
        return Objects.equals(id, ((ReimbursementTraining) o).id);
    }

    @Override
    public String toString() {
        return "pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraining[ id=" + id + " ]";
    }
    
}
